/**
 * Provides the starter code for the <strong>cs1302-omega</strong> project.
 */
module cs1302.omega {
    requires transitive java.logging;
    requires transitive javafx.controls;
    requires transitive com.google.gson;
	requires javafx.base;
	requires javafx.graphics;
	requires java.net.http;
    exports cs1302.api;
    exports cs1302.game;
    exports cs1302.omega;
} // module
