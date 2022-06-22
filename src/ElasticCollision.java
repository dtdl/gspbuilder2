import org.apache.log4j.Logger;


public class ElasticCollision {

	private static final Logger logger = Logger.getLogger(ElasticCollision.class);
	
	private int m1 = 10;
	private int m2 = 1;
	
	public int getCollisionCount(float m1, float m2) {
		logger.debug("m1: " + m1);
		logger.debug("m2: " + m2);
		
		int round = 0;
		int cnt = 0;
		
		float v1 = (float) -100.0;
		float v2 = (float) 0.0;
		
		while (v1 < v2) {
			round = round + 1;
			
			logger.info(" =========== start collision " + round);
			
			logger.debug(" before hit 1");
			logger.debug("v1: " + v1);
			logger.debug("v2: " + v2);
			
			
			float tv1 = v1;
			float tv2 = v2;
			
			// collision 
			v2 = ((m2-m1)*tv2 + tv1*2*m1)/(m1+m2);
			v1 = (tv1*(m1-m2) + 2*m2*tv2)/(m1+m2);
			cnt = cnt + 1;
			logger.debug(" after hit 1");
			logger.debug("v1: " + v1);
			logger.debug("v2: " + v2);
			
			// hit wall
			if (v2 < 0) {
				logger.debug(" after hit wall ");
				v2 = -1 * v2;
				cnt = cnt + 1;
				logger.debug("v1: " + v1);
				logger.debug("v2: " + v2);
			}
			
//			// collision again
//			tv1 = v1;
//			tv2 = v2;
//			v2 = ((m2-m1)*tv2 + tv1*2*m1)/(m1+m2);
//			v1 = (tv1*(m1-m2) + 2*m2*tv2)/(m1+m2);
//			
//			logger.debug(" after hit 2 ");
//			logger.debug("v1: " + v1);
//			logger.debug("v2: " + v2);
			logger.info(" =========== finish collision " + round);
			
		}
		
		return cnt;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ElasticCollision mc = new ElasticCollision();
		logger.info("total hit count: " + mc.getCollisionCount((float) 1000000, (float) 1));
		

	}

}
