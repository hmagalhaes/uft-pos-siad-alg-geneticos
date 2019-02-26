package coberturawifi.solution;

import coberturawifi.model.Chromosome;

interface CrossingStrategy {

	Chromosome cross(final Chromosome father, final Chromosome mother);

}
