package coberturawifi.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import coberturawifi.model.Coordinates;
import coberturawifi.model.Rect;
import coberturawifi.model.Rect.RectBuilder;

public class CoordinatesTest {

	@Test
	public void collidesWithTest() {
		final int x1 = 55;
		final int x2 = 67;
		final int y1 = 9000;
		final int y2 = 9887;

		final Rect rect = new RectBuilder().x(x1).y(y1).width(x2 - x1).height(y2 - y1).build();

		// collides
		{
			final Coordinates coords = new Coordinates(x1, y1);
			assertTrue(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x1, y2);
			assertTrue(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x2, y1);
			assertTrue(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x2, y2);
			assertTrue(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x1 + 1, y1 + 1);
			assertTrue(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x2 - 1, y2 - 1);
			assertTrue(coords.isWithin(rect));
		}

		// doesn't collide
		{
			final Coordinates coords = new Coordinates(x1 - 1, y1);
			assertFalse(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x1, y1 - 1);
			assertFalse(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x2 + 1, y2);
			assertFalse(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x2, y2 + 1);
			assertFalse(coords.isWithin(rect));
		}
		{
			final Coordinates coords = new Coordinates(x2 + 1, y2 + 1);
			assertFalse(coords.isWithin(rect));
		}
	}

}
