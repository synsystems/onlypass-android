# Coding Standards
To keep our code base consistent, we require all contributors to adhere to a strict set of coding standards. By following these standards:
- Developers can focus on the ideas behind the code without getting sidetracked by implementation details.
- Auto-formatting tools can be used without polluting version control with large diffs.
- Several classes of bugs can be eliminated before they even enter production.

Android projects contain several different types of files and resources. As such, the standards have been divided into:
- Java standards.
- Resource standards.
- Gradle standards.

A file is said to be compliant if and only if it follows the applicable standards outlined in this document, as well as the Android Studio code style definitions found [here](AndroidStudio-CodeStyle.xml).

## Java
The Java standards are applicable to all Java source files, except for dynamically generated sources which are explicitely exempt.

The following definitions are used consistently throughout the Java standards:
- The term *class* refers to standard Java classes, enums, interfaces and annotation types.
- The term *member* refers to the nested classes, fields, methods and constructors at the top level of a class. Initialiser blocks are not members.
- The term *comment* refers to all single line and multiline comments, but specifically excludes Javadoc.

### General
All Java source files:
- Use spaces for indentation.
- Are wrapped at 120 characters.
- Use UTF-8 encoding.

In regard to indentation, use two spaces for a new scope and four spaces for a continuation line.

### General naming rules
Never use Hungarian notation for anything. Ever.

Regarding abbreviations:
- Only use abbreviations that aid readability. Never use abbreviations just to save typing.
- Never shorten words by removing non-contiguous letters from the word (e.g. adapter to adptr).
- Only use unambiguous abbreviations which you would expect every programmer to know (e.g. auth, num, XML, ID and CSS etc.).

Regarding units:
- When representing quantities using types that lack implicit unit support, always append the unit to the name of the variable/method.
- Use full units since camel case can interfere with the metric system. For example, does lengthMMetres represent a length in millimetres or megametres?
- Always write units as plurals.
- Always use the metric system unless the context justifies non-metric units. For example, inches are a valid unit when referring to screen size.

Here's an example of a method declaration with correct unit usage:
```java
double getCurrentAmps(final int resistenceKiloOhms, final double voltageVolts)
```

Screen dimension units must always be abbreviated to:
- Px for pixels
- Dp for display independent pixels
- Sp for scaled pixels
- Pt for points
- In for inches

### Class declarations
The "SomeInterfaceImpl" and "ISomething" naming patterns are never used. These patterns are indicators of a broken class hierarchy.

Unit test classes are named the same as the class under test, but with "Test" prepended (e.g. TestStringFactory for StringFactory).

If the entire class declaration can fit on one line, keep it on one line. For example:
```java
public class BasicClass extends OtherClass implements SomeInterface {
    // contents
}
```

If the entire class declaration cannot fit on one line, wrap at both the `extends` keyword and the `implements` keyword. For example:
```java
public class MoreComplexClass
        extends SomeClassWithReallyLongName
        implements SomeInterfaceWithReallyLongName {

    // contents
}
```

If the interface list still cannot fit on one line, wrap the interfaces at the line limit. For example:
```java
public class EvenMoreComplexClass
        extends SomeClassWithExceptionallyLongName1,
        implements SomeInterfaceWithExceptionallyLongName, AnotherInterface,
        YetAnotherInterface, HowManyInterfacesDoesOneClassNeed {

    // contents
}
```

In the latter two cases, place a blank line between the declaration and the first member.

### Method declarations
All overridden methods must be annotated with `@Override`.

If the entire method declaration can fit on one line, keep it on one line. For example:
```java
public static void doSomething(final int i) throws IOException {
  // Some implementation
}
```

If the entire definition cannot fit on one line, wrap it at every parameter and at the throws keyword. For example:
```java
public static void doSomethingWithLotsOfArguments(
    int i1,
    int i2,
    int i3,
    int i4,
    int i5)
    throws IOException {

    // Some implementation
}
```

If the exception list still cannot fit on one line, wrap the exception list at the line limit. For example:
```java
public static void doSomethingWithLotsOfArguments(
    int i1,
    int i2,
    int i3,
    int i4,
    int i5)
    throws IOException, ClassNotFoundException, NullPointerException,
    IllegalArgumentException, IndexOutOfBoundsException {

    // Some implementation
}
```

In the latter two cases, place a blank line between the declaration and the first statement.

### Logging
We use the standard Android logging levels as follows:
- Error: The thread, process or application is about to crash. Generally this level is not used within the application and is reserved for application-wide crash wrappers.
- Warning: Something has occurred outside normal operating conditions, but the system can recover (potentially with degraded user experience). Examples include bad network responses and unreadable files.
- Info: Something has occurred which should be investigated in the future but does not pose a threat to stability, security, or the use experience.
- Debug: High level trace events. For example, "Auth token has expired. Refreshing."
- Verbose: Low level trace events. For example, "onCreate(Bundle) called with (null) on main thread".

Debug and verbose logs are never enabled in production, and must therefore only contain information that's relevant to development. 

Make sure logs are human readable and provide enough information to be useful to other developers. For example, instead of logging "Bad date entered" you should log "The user provided a date of birth that is in the future.".

Always use string-types for string substitutions in logging calls.
```
  // Will fail if the error code cannot be cast to decimal, and the error will be lost.
  Timber.d("Error occurred with code %d.", error.code());

  // The error code can always be cast to a string.
  Timber.d("Error occurred with code %s.", error.code());
```

### Fragments
If ever you need to use a fragment, first seek an alternative. If you still think a fragment is needed, see step 1. Repeat until an alternative is found.

### Miscellaneous rules
All variables that can be declared final, must be declared final. This includes the parameters of regular methods and constructors, but excludes those of unimplemented methods in abstact classes and interfaces.

Classes and methods are never declared final, with the exception of data-model classes. Other classes must never depend on final classes directly, instead they must depend on a non-final superclasses/interfaces.

Only use the `this` keyword when a member variable is shadowed by another variable.

Prefer to place comments on separate lines, rather than inline with code.

Never declare/instantiate more than one variable in a single line.

Make liberal use of whitespace. Every member must be separated by one blank line, and blank lines should be used within methods to aid readability wherever possible.

## Resources
The resource standards are applicable to all Android resource files, except for dynamically generated resources which are explicitely exempt.

### General
All XML resource files:
- Use spaces for indentation.
- Are wrapped at 120 characters.
- Use UTF-8 encoding.

In regard to indentation, use two spaces for a new scope and four spaces for a continuation line.

### Drawables
Prefer SVGs to PNGs.

Use the following prefixes to identify image types:
- Icons: icon_
- Photos: photo_
- View backgrouns: background_
- Animated vector drawables: animated_
- Other: misc_

Choose names that describe the drawable itself rather than its purpose, for example use "icon_white_exclamation_mark" instead of "icon_error".

XML drawable fields are exempt from the indentation and wrapping rules.

### Layouts
Layout files for activities and custom views are named the same as the respective classes, except underscores are used instead of custom views. For example, the layout file for 'LogInActivity' is 'log_in_activity.xml', and the the layout file for 'ExtendedTextView' is  'extended_text_view.xml'

Use the following terms when setting view IDs for view groups:
- Coordinator: A ViewGroup that contains one or more child views and provides coordination between the views.
- Group: A ViewGroup that contains one or more child views, so that those child views can be manipulated as a single view entity.
- Wrapper: A ViewGroup that wraps a single child view.

For example, a view group that contains a username EditText and a password EditText should have 'user_details_field_group' as its ID.

### Strings
Each Android module may have at most one strings resource file. Separating strings into multiple files creates the illusion of a namespace system, but ultimately just makes it harder to find specific strings.

Each string name has two parts: a group and a description. The group describes where the string is used in the app, and the description describes what the string is. For example, the main title of the LoginActivity is defined as:
```XML
<resources>
    <string name="login_activity_main_title">Please enter a username and password.</string>
</resources>
```

It's preferable for each string to be used in exactly one place in the app. If the circumstances make reuse a better option, then the group should describe the broadest use of the string. For example, strings which are used throughout the app for dialog boxes should be placed in the 'app' group, and strings used throughout the login feature should be placed in the 'login' group.

Separate groups with a single blank line, and don't use blank lines within a group.

String resource files are exempt form the wrapping rules.

### Colours
Colour resource names contain only:
- Lowercase letters
- Underscores

Separate words with underscores.

Use [this website](http://chir.ag/projects/name-that-color/) for naming colour resources.

## Gradle standards
The gradle standards are applicable to all gradle build files, including the main build file for each module and any additional build files.

All gradle files:
- Use spaces for indentation.
- Are wrapped at 120 characters.
- Use UTF-8 encoding.

In regard to indentation, use two spaces for a new scope and four spaces for a continuation line.