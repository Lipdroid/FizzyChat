package brad.lipuhossain.fizzychat.interfaces;

public interface AsyncCallback {
	// when asynctask done it call done()
	public void done(String result);
	
	// when asynctask begin it call progress()
	public void progress();
	
	public void onInterrupted(Exception e);
	
	public void onException(Exception e);
}
