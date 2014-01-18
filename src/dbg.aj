//import android.util.Log;

public aspect dbg {

		pointcut debug() : call (* *.*(*));
		
		//before() : debug() {
		//	Log.d("TAG", "Sending message...");
			
		//}
	
}
