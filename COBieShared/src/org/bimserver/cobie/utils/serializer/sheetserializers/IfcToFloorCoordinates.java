package org.bimserver.cobie.utils.serializer.sheetserializers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.bimserver.cobie.cobielite.COBIEType;
import org.bimserver.cobie.cobielite.COBIEType.Coordinates;
import org.bimserver.cobie.cobielite.CoordinateType;
import org.bimserver.cobie.utils.deserializer.modelhandlers.GeometryHandler;
import org.bimserver.cobie.utils.serializer.IfcCOBieSerializer;
import org.bimserver.cobie.utils.serializer.RectangleCoordinatesCalculator;
import org.bimserver.models.ifc2x3tc1.IfcBuildingStorey;
import org.bimserver.models.ifc2x3tc1.IfcLocalPlacement;
import org.bimserver.models.ifc2x3tc1.IfcSpace;
import org.bimserver.plugins.serializers.IfcModelInterface;
import org.bimserver.shared.cobie.COBieUtility;

public class IfcToFloorCoordinates extends IfcCOBieSerializer<CoordinateType,COBIEType.Coordinates,IfcBuildingStorey>
{
	private static final String COORDINATE_FORMAT_STRING = "%.3f%n";
	private static final int THREE_D_DIMENSIONS = 3;
	private static final String CATEGORY_POINT = "point";
	public IfcToFloorCoordinates(Coordinates cobieSection,
			IfcModelInterface model)
	{
		super(cobieSection, model);
	}

	@Override
	protected List<IfcBuildingStorey> getTopLevelModelObjects()
	{
		return model.getAll(IfcBuildingStorey.class);
	}

	@Override
	protected List<CoordinateType> serializeModelObject(
			IfcBuildingStorey modelObject)
	{
		List<CoordinateType> coordinatesWritten =
				new ArrayList<CoordinateType>();
		if(modelObject.isSetObjectPlacement())
		{
			if(modelObject.getObjectPlacement() instanceof IfcLocalPlacement)
			{
				Vector<Double> pointVector =
						GeometryHandler.vectorFromLocalPlacement
						((IfcLocalPlacement)modelObject.getObjectPlacement());
				coordinatesWritten.add(newFloorCoordinate(pointVector,modelObject));
			}
			
			
		}
		return coordinatesWritten;
	}
	
	private CoordinateType newFloorCoordinate(Vector<Double> pointVector,IfcBuildingStorey ifcBuildingStorey)
	{
		CoordinateType newCoordinate = cobieSection.addNewCoordinate();
		String name = IfcToFloor.nameFromBuildingStorey(ifcBuildingStorey);
		String createdBy = COBieUtility.getEmailFromOwnerHistory(ifcBuildingStorey.getOwnerHistory());
		Calendar createdOn = IfcToContact.getCreatedOn(ifcBuildingStorey.getOwnerHistory().
				getCreationDate());
		String category =  CATEGORY_POINT;

		String sheetName = COBieUtility.CobieSheetName.Floor.name();
		String rowName = name;
		String coordinateXAxis = COBieUtility.COBieNA;
		String coordinateYAxis = COBieUtility.COBieNA;
		String coordinateZAxis = COBieUtility.COBieNA;
		if(pointVector!=null && pointVector.size()==THREE_D_DIMENSIONS)
		{
			coordinateXAxis = String.format(COORDINATE_FORMAT_STRING,pointVector.get(0));
			coordinateYAxis = String.format(COORDINATE_FORMAT_STRING,pointVector.get(1));
			coordinateZAxis = String.format(COORDINATE_FORMAT_STRING,pointVector.get(2));
		}
		String extSystem = COBieUtility.getApplicationName(ifcBuildingStorey.getOwnerHistory());
		String extObject = COBieUtility.extObjectFromObjectDef(ifcBuildingStorey);
		String extId = COBieUtility.getCOBieString(ifcBuildingStorey.getGlobalId().getWrappedValue());
		String clockWiseRotation = COBieUtility.COBieNA;
		String elevationRotation = COBieUtility.COBieNA;
		String yawRotation = COBieUtility.COBieNA;
		
		initializeCoordinate(newCoordinate, name, createdBy, createdOn,
				category, sheetName, rowName, coordinateXAxis, coordinateYAxis,
				coordinateZAxis, extSystem, extObject, extId,
				clockWiseRotation, elevationRotation, yawRotation);
		return newCoordinate;
	}

	private void initializeCoordinate(CoordinateType newCoordinate,
			String name, String createdBy, Calendar createdOn, String category,
			String sheetName, String rowName, String coordinateXAxis,
			String coordinateYAxis, String coordinateZAxis, String extSystem,
			String extObject, String extId, String clockWiseRotation,
			String elevationRotation, String yawRotation)
	{
		newCoordinate.setName(name);
		newCoordinate.setCreatedBy(createdBy);
		newCoordinate.setCreatedOn(createdOn);
		newCoordinate.setCategory(category);
		newCoordinate.setSheetName(sheetName);
		newCoordinate.setRowName(rowName);
		newCoordinate.setCoordinateXAxis(coordinateXAxis);
		newCoordinate.setCoordinateYAxis(coordinateYAxis);
		newCoordinate.setCoordinateZAxis(coordinateZAxis);
		newCoordinate.setExtSystem(extSystem);
		newCoordinate.setExtObject(extObject);
		newCoordinate.setExtIdentifier(extId);
		newCoordinate.setClockwiseRotation(clockWiseRotation);
		newCoordinate.setElevationalRotation(elevationRotation);
		newCoordinate.setYawRotation(yawRotation);
	}

}
