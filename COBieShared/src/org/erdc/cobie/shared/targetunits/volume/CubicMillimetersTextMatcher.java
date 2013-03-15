package org.erdc.cobie.shared.targetunits.volume;

import org.bimserver.models.ifc2x3tc1.IfcUnit;
import org.erdc.cobie.shared.targetunits.UnitTextMatcher;

public class CubicMillimetersTextMatcher extends UnitTextMatcher
{

    public CubicMillimetersTextMatcher(String searchString)
    {
        super(searchString);

    }

    @Override
    protected String[] getTargetStringArray()
    {
        return VolumeStatics.Metric.CubicMillimeters.UNIT_STRINGS;
    }

    @Override
    public IfcUnit getTargetUnit()
    {
        return VolumeStatics.Metric.CubicMillimeters.getUnit();
    }

}