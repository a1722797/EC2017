package ec2017.ga.general;

public class Algorithm 
{
	private CrossOverOperator _xop;
	private MutateOperator _mop;
	private ParentSelectionMethod _psm;
	private SurvivorSelectionMethod _ssm;
	
	private double _pMutate = 1;
	private double _pXOver = 1;
	
	public Algorithm(CrossOverOperator xop, MutateOperator mop, ParentSelectionMethod psm, SurvivorSelectionMethod ssm)
	{
		if (xop == null && mop == null)
			throw new IllegalArgumentException("Population cannot be created without a cross-over or mutation operation");
		if (psm == null)
			throw new IllegalArgumentException("You must determine a parent selection method.");
		if (ssm == null)
			throw new IllegalArgumentException("Population cannot be created without a survivor selection method");
		
		setCrossOver(xop);
		setMutate(mop);
		setParentSelect(psm);
		setSurvivorSelect(ssm);
	}
	
	public CrossOverOperator getCrossOver() 
	{
		return _xop;
	}
	public void setCrossOver(CrossOverOperator xop) 
	{
		this._xop = xop;
	}
	public MutateOperator getMutate() 
	{
		return _mop;
	}
	public void setMutate(MutateOperator mop) 
	{
		this._mop = mop;
	}
	public ParentSelectionMethod getParentSelect() 
	{
		return _psm;
	}
	public void setParentSelect(ParentSelectionMethod psm) 
	{
		this._psm = psm;
	}
	public SurvivorSelectionMethod getSurvivorSelect() 
	{
		return _ssm;
	}
	public void setSurvivorSelect(SurvivorSelectionMethod ssm) 
	{
		this._ssm = ssm;
	}

	public double getPMutate() 
	{
		return _pMutate;
	}

	public void setPMutate(double _pMutate)
	{
		this._pMutate = _pMutate;
	}

	public double getPXOver() 
	{
		return _pXOver;
	}

	public void setPXOver(double _pXOver) 
	{
		this._pXOver = _pXOver;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (_xop != null)sb.append(_xop.getClass().getSimpleName());
		else sb.append("none");
		sb.append('-');
		if (_mop != null)sb.append(_mop.getClass().getSimpleName());
		else sb.append("none");
		sb.append('-');
		sb.append(_psm.getClass().getSimpleName());
		sb.append('-');
		sb.append(_ssm.getClass().getSimpleName());
		
		return sb.toString();
	}
}
