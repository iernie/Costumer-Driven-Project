The project is organized as follows:

	* Algorithms -:- all reduction,conlcusion, and learning algorithms, as well as distance metrics
	* dataObjects -:- all classes required to build a PolicyObject (P3P object)
	* dataStorage -:- all classes storing PolicyObjects- PolicyDatabase (local) and NetworkR (Server)
	* main -:- Gio (the glue) and PrivacyAdviser (original main and program flow). also commandline option reference
	* parser -:- Any parsing classes (for P3Ps)
	* sample -:- random code used to test an idea or see how a library works. irrelevent
	* test -:- Junit tests and other classes for testing code
	* ui -:- All user interface classes, and PrivacyAdviserGui with is both a user interface and main class (as it directs cycle flow)

Note:
	All but sample and test have been checked for documentation.
