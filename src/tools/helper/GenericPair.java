package tools.helper;

public class GenericPair<L, R>
{
	private L left;
	private R right;
	
	
	public GenericPair()
	{
	}
	
	public GenericPair(L left, R right)
	{
		this.setLeft(left);
		this.setRight(right);
	}

	public R getRight() {
		return right;
	}

	public void setRight(R right) {
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public void setLeft(L left) {
		this.left = left;
	}
	
	

}
