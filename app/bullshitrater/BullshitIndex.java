package bullshitrater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class BullshitIndex {
  private static final Pattern CLASS_NAME = Pattern.compile("(\\w+[\\/$])*(\\w+)\\.class");
  private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\d+");

  public static JarInfo ofJar(File jar) {
    return
        Stream.of(JarEntries.of(jar))
            .map(CLASS_NAME::matcher)
            .filter(Matcher::matches) //avoid directories
            .map(m -> m.replaceFirst("$2")) //get class names
            .filter(c -> !isAnonymousClass(c)) //exclude anonymous classes
            .collect(grabbingBullshit());
  }

  public static double ofClass(String className) {
    int words = className.replaceAll("[A-Z][A-Z]*([a-z_\\d]+)?", "!").length();
    return words > 1 ? words - 1 : 1;
  }

  static boolean isAnonymousClass(String className) {
    return ANONYMOUS_CLASS.matcher(className).matches();
  }

  private static class JarEntries {
    static String[] of(File jar) {
      try {
        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jar));
        List<String> classes = new ArrayList<>();
        while (true) {
          JarEntry entry = jarInputStream.getNextJarEntry();
          if (entry == null) break;
          classes.add(entry.getName());
        }
        return classes.toArray(new String[classes.size()]);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static Collector<String, ?, JarInfo> grabbingBullshit() {
    return new Collector<String, JarInfo, JarInfo>() {
      @Override
      public Supplier<JarInfo> supplier() {
        return JarInfo::new;
      }

      @Override
      public BiConsumer<JarInfo, String> accumulator() {
        return (s, c) -> { s.add(c, BullshitIndex.ofClass(c)); };
      }

      @Override
      public BinaryOperator<JarInfo> combiner() {
        return JarInfo::combine;
      }

      @Override
      public Function<JarInfo, JarInfo> finisher() {
        return JarInfo::evaluate;
      }

      @Override
      public Set<Characteristics> characteristics() {
        return Collections.emptySet();
      }
    };
  }

  public static void main(String[] args) {
    JarInfo jarInfo = ofJar(new File("/home/andrey/work/play-github/framework/play-1.3.x-6550295.jar"));
    System.out.println(jarInfo.bullshitIndex);
  }
}
