package com.jantox.siege.colsys;

public class CollisionSystem {

	public static boolean collides(CollisionMask a, CollisionMask b) {
		if(a instanceof Circle) {
			if(b instanceof Circle) {
				return CollisionSystem.collides((Circle) a, (Circle) b);
			} else if(b instanceof AABB) {
				return CollisionSystem.collides((Circle) a, (AABB) b);
			}
		} else if(a instanceof AABB) {
			if(b instanceof Circle) {
				return CollisionSystem.collides((Circle) b, (AABB) a);
			} else if(b instanceof AABB) {
				return collides((AABB) a, (AABB) b);
			}
		}
		
		return false;
	}
	
	private static boolean collides(Circle a, Circle b) {
		return (a.getPosition().distanceSquared(b.getPosition()) < a.getRadius() * a.getRadius() + b.getRadius() * b.getRadius());
	}
	
	private static boolean collides(AABB a, AABB b) {
		if (Math.abs(a.getPosition().x - b.getPosition().x) > (a.r[0] + a.r[0])) return false;
		if (Math.abs(a.getPosition().y - b.getPosition().y) > (b.r[1] + b.r[1])) return false;
		return false;
	}
	
	private static boolean collides(Circle a, AABB b) {
		double circleDistanceX = Math
				.abs(a.getPosition().x - b.getPosition().x);
		double circleDistanceY = Math
				.abs(a.getPosition().y - b.getPosition().y);

		if (circleDistanceX > (b.r[0] / 2 + a.getRadius())) {
			return false;
		}
		if (circleDistanceY > (b.r[1] / 2 + a.getRadius())) {
			return false;
		}

		if (circleDistanceX <= (b.r[0] / 2)) {
			return true;
		}
		if (circleDistanceY <= (b.r[1] / 2)) {
			return true;
		}

		double cornerDistance_sq = ((circleDistanceX - b.r[0] / 2) * b.r[0] / 2)
				+ ((circleDistanceY - b.r[1] / 2) * b.r[1] / 2);

		return (cornerDistance_sq <= (a.getRadius() * a.getRadius()));
	}
	
}
