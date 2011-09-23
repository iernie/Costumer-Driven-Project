import java.util.ArrayList;


public class bitmapDistance implements DistanceMetric{
	//private int[] purpMap;
	//private int[] recipMap;
	//private int[] retMap;
	
	bitmapDistance(){
	//	purpMap = new int[12];
	//	recipMap = new int[6];
	//	retMap = new int[5];
	}
	
	private int[] MakeRecipMap(ArrayList<Recipient> list){
		int[] Map = new int[6];
		for(int i=0;i<list.size();i++){
			switch (list.get(i)){
			case OURS: //weights can come in here 2
				Map[0]=1;
				break;
			case DELIVERY:
				Map[1]=1;
				break;
			case SAME:
				Map[2]=1;
				break;
			case OTHER_RECIPIENT:
				Map[3]=1;
				break;
			case UNRELATED:
				Map[4]=1;
				break;
			case PUBLIC:
				Map[5]=1;
				break;
			default:break;
			}
		}
		return Map;
	}
	private int[] MakePurposeMap(ArrayList<Purpose> list){
		int[] Map = new int[12];
		for(int i=0;i<list.size();i++){
			switch (list.get(i)){
			case CURRENT:
				Map[0]=1;//weights can come in here 2
				break;
			case ADMIN:
				Map[1]=1;
				break;
			case DEVELOP:
				Map[2]=1;
				break;
			case TAILORING:
				Map[3]=1;
				break;
			case PSEUDO_ANALYSIS:
				Map[4]=1;
				break;
			case PSEUDO_DECISION:
				Map[5]=1;
				break;
			case INDIVIDUAL_ANALYSIS:
				Map[6]=1;
				break;
			case INDIVIDUAL_DECISION:
				Map[7]=1;
				break;
			case CONTACT:
				Map[8]=1;
				break;
			case HISTORICAL:
				Map[9]=1;
				break;
			case TELEMARKETING:
				Map[10]=1;
				break;
			case OTHER_PURPOSE:
				Map[11]=1;
				break;
			default:break;
			}
		}
		return Map;
	}
	
	private int[] MakeRetentionMap(ArrayList<Retention> list){
		int[] Map = new int[5];
		for(int i=0;i<list.size();i++){
			switch (list.get(i)){
			case NO_RETENTION: //weights can come in here 2
				Map[0]=1;
				break;
			case STATED_PURPOSE:
				Map[1]=1;
				break;
			case LEGAL_REQUIREMENT:
				Map[2]=1;
				break;
			case BUSINESS_PRACTICES:
				Map[3]=1;
				break;
			case INDEFINITELY:
				Map[4]=1;
				break;
			default:break;
			}
		}
		return Map;
	}
	
	@Override
	public double getDistRecip(Case a, Case b) {
		int[] MapA, MapB;
		MapA = MakeRecipMap(a.getRecip());
		MapB = MakeRecipMap(b.getRecip());
		double dis=0;
		
		for(int i=0;i<6;i++){
			if(MapA!=MapB){
				dis=dis+1; //for now... weights are jet not operational.
			}
		}
		
		
		return dis;// dis*weight later
	}

	@Override
	public double getDistReten(Case a, Case b) {
		int[] MapA, MapB;
		MapA = MakeRetentionMap(a.getRet());
		MapB = MakeRetentionMap(b.getRet());
		double dis=0;
		
		for(int i=0;i<12;i++){
			if(MapA!=MapB){
				dis=dis+1; //for now... weights are jet not operational.
			}
		}
		
		
		return dis;// dis*weight later
	}

	@Override
	public double getDistPurpose(Case a, Case b) {
		int[] MapA, MapB;
		MapA = MakePurposeMap(a.getPurp());
		MapB = MakePurposeMap(b.getPurp());
		double dis=0;
		
		for(int i=0;i<12;i++){
			if(MapA!=MapB){
				dis=dis+1; //for now... weights are jet not operational.
			}
		}
		
		
		return dis;// dis*weight later
	}

	@Override
	public double getWeigth(Recipient r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWeigth(Retention r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWeigth(Purpose r) {
		// TODO Auto-generated method stub
		return 0;
	}

}
