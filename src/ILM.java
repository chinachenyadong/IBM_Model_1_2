public class ILM
{
	int i;
	int l;
	int m;

	ILM(int i, int l, int m)
	{
		this.i = i;
		this.l = l;
		this.m = m;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + l;
		result = prime * result + m;
		return result;
	}

	public int getI()
	{
		return i;
	}

	public void setI(int i)
	{
		this.i = i;
	}

	public int getL()
	{
		return l;
	}

	public void setL(int l)
	{
		this.l = l;
	}

	public int getM()
	{
		return m;
	}

	public void setM(int m)
	{
		this.m = m;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ILM other = (ILM) obj;
		if (i != other.i)
			return false;
		if (l != other.l)
			return false;
		if (m != other.m)
			return false;
		return true;
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
