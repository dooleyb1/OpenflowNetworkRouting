JFLAGS = -cp
JC = javac
JVM = java

BUILD = *:. 

.SUFFIXES: .java .class .jar

default:
	$(JC) $(JFLAGS) $(BUILD) *.java		

system: routers

routers: endusers
	gnome-terminal --geometry 30x10+0+0 -t "Router 1(40789)" -e "$(JVM) Router 40789"
	gnome-terminal --geometry 30x10+340+0 -t "Router 2(40790)" -e "$(JVM) Router 40790"
	gnome-terminal --geometry 30x10+615+0 -t "Router 3(40791)" -e "$(JVM) Router 40791"
	gnome-terminal --geometry 30x10+0+225 -t "Router 4(40792)" -e "$(JVM) Router 40792"
	gnome-terminal --geometry 30x10+340+225 -t "Router 5(40793)" -e "$(JVM) Router 40793"
	gnome-terminal --geometry 30x10+615+225 -t "Router 6(40794)" -e "$(JVM) Router 40794"

enduser:
	gnome-terminal --geometry 30x10+1300+475 -t "HELLO" -e "$(JVM) EndUser"


endusers: controller
	gnome-terminal --geometry 30x10+0+460 -t "End User 1(40700)" -e "$(JVM) EndUser 40700"
	gnome-terminal --geometry 30x10+340+460 -t "End User 2(40701)" -e "$(JVM) EndUser 40701"
	gnome-terminal --geometry 30x10+615+460 -t "End User 3(40702)" -e "$(JVM) EndUser 40702"
	gnome-terminal --geometry 30x10+0+668 -t "End User 4(40703)" -e "$(JVM) EndUser 40703"
	gnome-terminal --geometry 30x10+340+668 -t "End User 5(40704)" -e "$(JVM) EndUser 40704"
	gnome-terminal --geometry 30x10+615+668 -t "End User 6(40705)" -e "$(JVM) EndUser 40705"
	gnome-terminal --geometry 30x10+0+870 -t "End User 7(40706)" -e "$(JVM) EndUser 40706"
	gnome-terminal --geometry 30x10+340+870 -t "End User 8(40707)" -e "$(JVM) EndUser 40707"
	gnome-terminal --geometry 30x10+615+870 -t "End User 9(40708)" -e "$(JVM) EndUser 40708"

controller:
	gnome-terminal --geometry 30x10+1300+475 -t "Controller" -e "$(JVM) Controller"

clean: 
	$(RM) *.class

##Need To compile project with the juinity jar
##To run need to run the Test file with the hamcrest and Junit

