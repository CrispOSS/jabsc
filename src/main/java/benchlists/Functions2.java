package benchlists;

import static abs.api.cwi.Functional.Pair;
import static abs.api.cwi.Functional.fromJust;

import java.util.Optional;

/**
 * @deprecated Exists only to support custom data declarations
 *             to do benchmarks.
 */
@Deprecated
public class Functions2 {

  static interface Map_<A, B> {
  }

  static class EmptyMap_<A, B> implements Map_<A, B> {
  }

  static class InsertAssoc_<A, B> implements Map_<A, B> {
    public final Pair<A, B> pair;
    public final Map_<A, B> map;

    public InsertAssoc_(Pair<A, B> pair, Map_<A, B> map) {
      this.pair = pair;
      this.map = map;
    }
  }

  public static <A, B> Map_<A, B> put_(final Map_<A, B> ms, final A k, final B v) {
    if (ms instanceof EmptyMap_ || ms == null) {
      return new InsertAssoc_(Pair(k, v), new EmptyMap_<>());
    }
    if (ms instanceof InsertAssoc_) {
      InsertAssoc_<A, B> is = (InsertAssoc_<A, B>) ms;
      if (k.equals(is.pair.getFirst())) {
        return new InsertAssoc_<>(Pair(k, v), is.map);
      } else {
        return new InsertAssoc_<>(Pair(k, v), put_(is.map, k, v));
      }
    }
    throw new IllegalArgumentException();
  }

  public static <A, B> B lookupUnsafe_(Map_<A, B> ms, A k) {
    return fromJust(lookup_(ms, k));
  }

  public static <A, B> Optional<B> lookup_(Map_<A, B> ms, A k) {
    if (ms instanceof EmptyMap_) {
      return Optional.ofNullable(null);
    }
    if (ms instanceof InsertAssoc_) {
      InsertAssoc_<A, B> is = (InsertAssoc_<A, B>) ms;
      if (k.equals(is.pair.getFirst())) {
        return Optional.of(is.pair.getSecond());
      } else {
        return lookup_(is.map, k);
      }
    }
    throw new IllegalArgumentException();
  }
}
