# Compiler flags
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class

# Directory containing .java files
SRCDIR = src
# Directory where .class files should be generated 
BINDIR = $(SRCDIR)/lab1

# .java files to compile
JAVA_FILES = $(BINDIR)/Category.java $(BINDIR)/IRBuilder.java $(BINDIR)/Main.java $(BINDIR)/Operations.java $(BINDIR)/Parser.java $(BINDIR)/Scanner.java $(BINDIR)/Token.java $(BINDIR)/Renamer.java

# Target entry for creating .class files from .java files
build: $(JAVA_FILES)
	$(JC) $(JFLAGS) -d . $(JAVA_FILES)

# Clean up the .class files
clean:
	$(RM) $(BINDIR)/*.class
