
public class distanceMetricTest implements DistanceMetric{

	public double getDistResip(Case a, Case b) {


		return 0;
	}

	public double getDistReten(Case a, Case b) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDistPurpose(Case a, Case b) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Just used some random return numbers, should be changed something meaningful later
	 */
	public double getWeigth(Recipient r) {
		switch(r){
		case OURS:
			return 1;
		case DELIVERY:
			return 2;
		case SAME:
			return 3;
		case OTHER_RECIPIENT:
			return 4;
		case UNRELATED:
			return 5;
		case PUBLIC:
			return 6;
			
		}
		return -1;
	}

	public double getWeigth(Retention r) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getWeigth(Purpose r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalDistance(PolicyObject a, PolicyObject b) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
