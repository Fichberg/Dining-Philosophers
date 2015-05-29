JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Dinner.java \
	Philosopher.java \
	InputReader.java
default: classes
classes: $(CLASSES:.java=.class)
clean:
	$(RM) *.class
	$(RM) logs/*.log