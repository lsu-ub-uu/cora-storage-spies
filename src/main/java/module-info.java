/**
 * The testspies module provides common spies used when testing Cora projects.
 */
module se.uu.ub.cora.storage.spies {

	requires org.testng;
	requires se.uu.ub.cora.logger;
	requires transitive se.uu.ub.cora.testutils;

	exports se.uu.ub.cora.storage.spies;
}