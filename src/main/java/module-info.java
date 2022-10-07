/**
 * The testspies module provides common spies used when testing Cora projects.
 */
module se.uu.ub.cora.storage.spies {

	requires org.testng;
	requires se.uu.ub.cora.logger;
	requires se.uu.ub.cora.data.spies;
	requires transitive se.uu.ub.cora.testutils;
	requires transitive se.uu.ub.cora.storage;
	requires se.uu.ub.cora.data;

	exports se.uu.ub.cora.storage.spies;
}