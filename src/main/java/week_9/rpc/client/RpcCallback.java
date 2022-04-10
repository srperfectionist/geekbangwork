package week_9.rpc.client;

public interface RpcCallback {

	void success(Object result);
	
	void failure(Throwable cause);
	
}
