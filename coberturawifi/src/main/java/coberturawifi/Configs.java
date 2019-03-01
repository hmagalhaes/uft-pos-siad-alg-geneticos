package coberturawifi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configs {

	public static final String TILE_SIZE_IN_METERS = "blueprint.tile.size.in.meters";
	public static final String GENERATION_COUNT = "generation.count";
	public static final String REPRESENTATION_TYPE = "representation.type";

	public static final String RESULT_SOLUTION_COUNT = "result.solution.count";
	public static final String RESULT_SOLUTION_FILE_PATTERN = "result.solution.file.pattern";
	public static final String RESULT_SOLUTION_COVERED_TILES_FILE_PATTERN = "result.solution.tiles.file.pattern";
	public static final String RESULT_GRID_ALL_FILE_PATTERN = "result.grid.all.file.pattern";
	public static final String RESULT_GRID_SELECTED_FILE_PATTERN = "result.grid.selected.file.pattern";
	public static final String RESULT_FILE_FORMAT = "result.file.format";
	public static final String RESULT_COLOR_GRID = "result.color.grid";
	public static final String RESULT_COLOR_RADIUS = "result.color.radius";
	public static final String RESULT_COLOR_CROSS = "result.color.cross";
	public static final String RESULT_OUTPUT_FOLDER = "result.output.folder";
	public static final String RESULT_PRINT_GRID = "result.print.grid";

	public static final String POPULATION_SIZE = "population.size";
	public static final String POPULATION_RATIO_MUTANT = "population.ratio.mutant";
	public static final String POPULATION_RATIO_CROSSED = "population.ratio.crossed";
	public static final String POPULATION_RATIO_ELITE = "population.ratio.elite";
	public static final String MUTATION_BITS_RATIO = "mutation.bits.ratio";
	public static final String STRATEGY_CROSSING = "strategy.crossing";

	public static final String BLUEPRINT_REQUIRED_AREA_COLOR = "blueprint.required.area.color";
	public static final String BLUEPRINT_INPUT_FILE = "blueprint.input.file";
	public static final String BLUEPRINT_WIDTH_IN_METERS = "blueprint.width.meters";
	public static final String BLUEPRINT_HEIGHT_IN_METERS = "blueprint.height.meters";

	public static final String ACCESS_POINT_QUANTITY = "accesspoint.quantity";
	public static final String ACCESS_POINT_RADIUS_IN_METERS = "accesspoint.cover.radius.meters";

	public static final String CONFIGS_PROJECT_FILE = "configs.project.file";

	//
	public static final String CONFIG_FILE_ENV_ENTRY = "app.config";

	private static final String DEFAULT_CONFIG_FILE = "configs.properties";

	private static Configs instance;

	private Properties props;

	public static Configs getInstance() {
		if (instance == null) {
			instance = new Configs();
		}
		return instance;
	}

	private String getMainConfigFile() {
		final String custom = System.getProperty(CONFIG_FILE_ENV_ENTRY);
		if (custom == null || custom.isEmpty()) {
			return DEFAULT_CONFIG_FILE;
		}
		return custom;
	}

	private Properties loadProps(final String configFile) {
		System.out.println("Loading configs from => " + configFile);

		final Properties props = new Properties();
		try (InputStream is = new FileInputStream(configFile)) {
			props.load(is);
		} catch (IOException ex) {
			throw new RuntimeException("Error whilst loading configs", ex);
		}
		return props;
	}

	private Properties loadProps() {
		final String mainFile = getMainConfigFile();
		final Properties mainProps = loadProps(mainFile);

		final String projectFile = mainProps.getProperty(CONFIGS_PROJECT_FILE);
		final Properties projectProps = loadProps(projectFile);

		mainProps.putAll(projectProps);

		return mainProps;
	}

	public String getString(String key) {
		if (props == null) {
			props = loadProps();
		}
		return props.getProperty(key);
	}

	public short getShort(String key) {
		return Short.parseShort(getString(key));
	}

	public int getInt(String key) {
		return Integer.parseInt(getString(key));
	}

	public byte getByte(String key) {
		return Byte.parseByte(getString(key));
	}

	public float getFloat(String key) {
		return Float.parseFloat(getString(key));
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

}
