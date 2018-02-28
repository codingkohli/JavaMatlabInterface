import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import matlabcontrol.*;
import matlabcontrol.extensions.MatlabTypeConverter;
import matlabcontrol.extensions.MatlabNumericArray;



public class DataConnector {

	public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException {
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		//System.out.println(java.lang.System.getProperty("java.class.version"));
		/* Declaring the connection*/
		MatlabProxyFactory factory = new MatlabProxyFactory();
		MatlabProxy proxy = factory.getProxy();
		
		proxy.eval("disp('Connecting to Matlab from Java')");
		
		/* Sending receiving the frames*/
		proxy.eval("frame = rand(4,3,2) ");
		proxy.eval("disp('Specific value in matlab array')");
		proxy.eval("disp(['entry :' num2str(frame(3,2,1))])");
		
		/*Receiveing the array from Matlab */
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		MatlabNumericArray recFrame = processor.getNumericArray("frame");
		
		/*Printing the same value */
		int indxs[] = {2,1,0};
		System.out.println("entry : " + recFrame.getRealValue(indxs));
		
		/* Applying the converse method 
		 * Array in java and send to matlab for some operations*/
		
		/* Creating a frame in java*/
		double [][] javFrame = new double [][] {{ 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		System.out.println("The current java frame is :");
		for(int i = 0 ; i < javFrame.length;i++)
		{
			System.out.println(Arrays.toString(javFrame[i]));
		}
		
		processor.setNumericArray("javMatFrame", new MatlabNumericArray(javFrame, null));
		proxy.eval("javMatFrame = transpose(javMatFrame)");
		
		double [][] transjavFrame = processor.getNumericArray("javMatFrame").getRealArray2D();
		
		System.out.println("The operated and transposed frame is :");
		for(int i = 0; i < transjavFrame.length;i++)
		{
			System.out.println(Arrays.toString(transjavFrame[i]));
		}
		
		
		
		
		/* Disconnecting from Matlab*/
		proxy.disconnect();
		
	}

}
