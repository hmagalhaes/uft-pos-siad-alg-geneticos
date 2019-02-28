package coberturawifi.solution.bitsrepresentation;

import coberturawifi.model.BitsChromosome;

interface BitsCrossingStrategy {

	BitsChromosome cross(final BitsChromosome father, final BitsChromosome mother);

}
