package br.eti.hmagalhaes.coberturawifi.solution;

import br.eti.hmagalhaes.coberturawifi.model.Chromosome;

interface CrossingStrategy {

	Chromosome cross(final Chromosome father, final Chromosome mother);

}
