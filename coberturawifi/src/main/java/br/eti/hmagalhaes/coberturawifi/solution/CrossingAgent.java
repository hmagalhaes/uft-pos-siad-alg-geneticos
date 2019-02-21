package br.eti.hmagalhaes.coberturawifi.solution;

import java.util.List;

import br.eti.hmagalhaes.coberturawifi.model.Chromosome;

interface CrossingAgent {

	List<Chromosome> crossPopulation(final List<Chromosome> population);

}
