package org.erdc.cobie.shared.bimserver.unit.length;

import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Factory;
import org.bimserver.models.ifc2x3tc1.IfcSIPrefix;
import org.bimserver.models.ifc2x3tc1.IfcSIUnit;
import org.bimserver.models.ifc2x3tc1.IfcSIUnitName;
import org.bimserver.models.ifc2x3tc1.IfcUnit;
import org.bimserver.models.ifc2x3tc1.IfcUnitEnum;
import org.erdc.cobie.shared.bimserver.unit.UnitTextMatcher;
import org.erdc.cobie.shared.unit.LinearUnit;

public class MillimeterTextMatcher extends UnitTextMatcher
{
    public static IfcUnit getTargetUnitStatic()
    {
        IfcSIUnit millimeters = Ifc2x3tc1Factory.eINSTANCE.createIfcSIUnit();
        millimeters.setName(IfcSIUnitName.METRE);
        millimeters.setUnitType(IfcUnitEnum.LENGTHUNIT);
        millimeters.setPrefix(IfcSIPrefix.MILLI);
        return millimeters;
    }

    public MillimeterTextMatcher(String searchString)
    {
        super(searchString);
    }

    @Override
    protected String[] getTargetStringArray()
    {
    	return LinearUnit.MILLIMETERS.getMeasurable().getSynonymousNames();
    }

    @Override
    public IfcUnit getTargetUnit()
    {
        return getTargetUnitStatic();
    }

}
