class WordPair
{
	String english = null;
	String espana = null;

	WordPair(String english, String espana)
	{
		this.english = english;
		this.espana = espana;
	}

	public String getEnglish()
	{
		return english;
	}

	public void setEnglish(String english)
	{
		this.english = english;
	}

	public String getEspana()
	{
		return espana;
	}

	public void setEspana(String espana)
	{
		this.espana = espana;
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((english == null) ? 0 : english.hashCode());
		result = prime * result + ((espana == null) ? 0 : espana.hashCode());
		return result;
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
		WordPair other = (WordPair) obj;
		if (english == null)
		{
			if (other.english != null)
				return false;
		}
		else if (!english.equals(other.english))
			return false;
		if (espana == null)
		{
			if (other.espana != null)
				return false;
		}
		else if (!espana.equals(other.espana))
			return false;
		return true;
	}

}
