package jabsc;

import java.util.function.Function;

import javax.lang.model.element.Element;

/**
 * A supplier for {@link JavaWriter} that provides separate
 * writers for every top-level Java {@link Element}. It is a
 * {@link Function} from the type name (String) to the required
 * Java writer.
 */
interface JavaWriterSupplier extends Function<String, JavaWriter> {

}
