/**
* Holds part of p3p policy, a bit with data type (primary value), recipient, retention, purpose, and a distance function
*
*/

class Case{
	private String data;
	private Recipient[] recip;
	private Purpose[] purp;
	private Retention[] ret;
	



	Case(String name){
	
		data = name;
	}
	
	public void addRecip(Recipient newrecip){
		Recipient newArray[]= new Recipient[recip.length+1];
		for(int i=0;i<recip.length;i++){
			newArray[i]=recip[i];
		}
		newArray[recip.length]=newrecip;
		this.recip=newArray;
	}
	
	public void addResip(Recipient Newrecip[]){
		Recipient newArray[]= new Recipient[recip.length+Newrecip.length];
		for(int i=0;i<recip.length;i++){
			newArray[i]=recip[i];
		}
		for(int d=0;d<Newrecip.length;d++){
			newArray[d+recip.length]=Newrecip[d+recip.length];
		}
		this.recip=newArray;
	}
	
	public Recipient[] getRecip(){
		return recip;
	}
	
	public Recipient getRecip(int i){
		if(i>0&&i<recip.length)return recip[i];
		else return null;
			//throw new OutOfBoundsException();    //this we need to think about
	}
	
	public void addPurp(Purpose newpurp){
		Purpose newArray[]= new Purpose[purp.length+1];
		for(int i=0;i<purp.length;i++){
			newArray[i]=purp[i];
		}
		newArray[purp.length]=newpurp;
		this.purp=newArray;
	}
	
	public void addPurp(Purpose Newpurp[]){
		Purpose newArray[]= new Purpose[purp.length+Newpurp.length];
		for(int i=0;i<purp.length;i++){
			newArray[i]=purp[i];
		}
		for(int d=0;d<Newpurp.length;d++){
			newArray[d+purp.length]=Newpurp[d+purp.length];
		}
		this.purp=newArray;
	}
	
	public Purpose[] getPurp(){
		return purp;
	}
	
	public Purpose getPurp(int i){
		if(i>0&&i<purp.length)return purp[i];
		else return null;
			//throw new OutOfBoundsException();    //this we need to think about
	}
	
	public void addRet(Retention newret){
		Retention newArray[]= new Retention[ret.length+1];
		for(int i=0;i<ret.length;i++){
			newArray[i]=ret[i];
		}
		newArray[ret.length]=newret;
		this.ret=newArray;
	}
	
	public void addret(Retention Newret[]){
		Retention newArray[]= new Retention[ret.length+Newret.length];
		for(int i=0;i<ret.length;i++){
			newArray[i]=ret[i];
		}
		for(int d=0;d<Newret.length;d++){
			newArray[d+ret.length]=Newret[d+ret.length];
		}
		this.ret=newArray;
	}
	
	public Retention[] getRet(){
		return ret;
	}
	
	public Retention getret(int i){
		if(i>0&&i<ret.length)return ret[i];
		else return null;
			//throw new OutOfBoundsException();    //this we need to think about
	}
}