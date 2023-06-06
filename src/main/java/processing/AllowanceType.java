package processing;

/**
 *  The AllowanceType enumeration allows expanding the traditional boolean logic to also include an
 *  "If Necessary" type. This allows configurations to be either strict or allow the user to use a particular
 *  feature if it is absolutely necessary. In that case, the user will be warned about the use, and it will
 *  be added to the user's checklist.
 *  An example use of this is only allowing italic text if absolutely necessary in MLA9.
 *
 *  @author Gino Glink
 *  @since 1.0
 *  @version 1.0
 */
public enum AllowanceType {

    /**
     *  YES allows the user to use a particular feature without the compiler meddling in any way.
     *  If used, the compiler will not print warnings or add it to the user's checklist.
     */
    YES,

    /**
     *  IF_NECESSARY is the third type, which allows the user to use a particular feature,
     *  however the compiler will warn the user and add it to the user's checklist.
     */
    IF_NECESSARY,

    /**
     * NO forbids the user from using a particular feature.
     * If used, the compiler will halt and throw an error.
     */
    NO

}
