package ec2017.ga.general;

/**
 * This class acts as a data structure to hold the algorithm configuration which
 * is to be applied to a Population.
 * @author pat
 *
 */
public class Algorithm 
{
	private CrossOverOperator _xop;
	private MutateOperator _mop;
	private ParentSelectionMethod _psm;
	private SurvivorSelectionMethod _ssm;
	
	// Setting both the probability of mutation and cross over to 1 
	// seemed to be more efficient than the default values in our tests.
	private double _pMutate = 1;
	private double _pXOver = 1;
	
	/**
	 * Constructor.
	 * @param xop Cross over operator. Required if the mutate operator is null.
	 * @param mop Mutate operator. Required if the cross over operator is null.
	 * @param psm Parent selection method. Required.
	 * @param ssm Survivor selection method. Required.
	 */
	public Algorithm(CrossOverOperator xop, MutateOperator mop, ParentSelectionMethod psm, SurvivorSelectionMethod ssm)
	{
		// Do some checking to validate against null values.
		if (xop == null && mop == null)
			throw new IllegalArgumentException("Population cannot be created without a cross-over or mutation operation");
		if (psm == null)
			throw new IllegalArgumentException("You must determine a parent selection method.");
		if (ssm == null)
			throw new IllegalArgumentException("Population cannot be created without a survivor selection method");
		
		// Set our values.
		setCrossOver(xop);
		setMutate(mop);
		setParentSelect(psm);
		setSurvivorSelect(ssm);
	}
	
	/**
	 * 
	 * @return Cross over operator for this algorithm.
	 */
	public CrossOverOperator getCrossOver() 
	{
		return _xop;
	}
	
	/**
	 * 
	 * @param xop Cross over operator for this algorithm.
	 */
	public void setCrossOver(CrossOverOperator xop) 
	{
		this._xop = xop;
	}
	
	/**
	 * 
	 * @return Mutate operator for this algorithm
	 */
	public MutateOperator getMutate() 
	{
		return _mop;
	}
	
	/**
	 * 
	 * @param mop Mutate operator for this algorithm
	 */
	public void setMutate(MutateOperator mop) 
	{
		this._mop = mop;
	}
	
	/**
	 * 
	 * @return Parent selection method for this algorithm
	 */
	public ParentSelectionMethod getParentSelect() 
	{
		return _psm;
	}
	
	/**
	 * 
	 * @param psm Parent selection method for this algorithm
	 */
	public void setParentSelect(ParentSelectionMethod psm) 
	{
		this._psm = psm;
	}
	
	/**
	 * 
	 * @return Survivor selection method for this algorithm
	 */
	public SurvivorSelectionMethod getSurvivorSelect() 
	{
		return _ssm;
	}
	
	/**
	 * 
	 * @param ssm Survivor selection method for this algorithm
	 */
	public void setSurvivorSelect(SurvivorSelectionMethod ssm) 
	{
		this._ssm = ssm;
	}

	/**
	 * 
	 * @return Probability of mutation being performed.
	 */
	public double getPMutate() 
	{
		return _pMutate;
	}

	/**
	 * 
	 * @param _pMutate Probability of mutation being performed.
	 */
	public void setPMutate(double _pMutate)
	{
		this._pMutate = _pMutate;
	}

	/**
	 * 
	 * @return Probability of cross over being performed.
	 */
	public double getPXOver() 
	{
		return _pXOver;
	}

	/**
	 * 
	 * @param _pXOver Probability of cross over being performed.
	 */
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
