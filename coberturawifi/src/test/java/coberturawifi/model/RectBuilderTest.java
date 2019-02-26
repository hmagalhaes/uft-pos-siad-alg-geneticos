package coberturawifi.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import coberturawifi.model.Rect;
import coberturawifi.model.Rect.RectBuilder;

public class RectBuilderTest {

	@Test
	public void testBuilding() {
		final Rect rect = new RectBuilder().x(13).width(37).y(22).height(28).build();
		assertEquals(13, rect.x1);
		assertEquals(50, rect.x2);
		assertEquals(22, rect.y1);
		assertEquals(50, rect.y2);
	}

}
