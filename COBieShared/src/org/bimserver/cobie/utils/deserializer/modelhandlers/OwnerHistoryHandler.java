package org.bimserver.cobie.utils.deserializer.modelhandlers;

import java.util.Calendar;
import java.util.Date;

import org.bimserver.cobie.cobielite.ContactType;

import org.bimserver.cobie.utils.deserializer.COBieIfcModel;
import org.bimserver.cobie.utils.deserializer.ContactDeserializer;
import org.bimserver.cobie.utils.stringwriters.DeserializerStaticStrings;
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Factory;
import org.bimserver.models.ifc2x3tc1.IfcActorRole;
import org.bimserver.models.ifc2x3tc1.IfcApplication;
import org.bimserver.models.ifc2x3tc1.IfcChangeActionEnum;
import org.bimserver.models.ifc2x3tc1.IfcOrganization;
import org.bimserver.models.ifc2x3tc1.IfcOwnerHistory;
import org.bimserver.models.ifc2x3tc1.IfcPerson;
import org.bimserver.models.ifc2x3tc1.IfcPersonAndOrganization;
import org.bimserver.models.ifc2x3tc1.IfcPostalAddress;
import org.bimserver.models.ifc2x3tc1.IfcTelecomAddress;
import org.bimserver.plugins.serializers.OidProvider;
import org.bimserver.shared.cobie.COBieUtility;


public class OwnerHistoryHandler
{
	private static final String APPLICATION_ORGANIZATION_PREFIX = "develoeprs of ";
	private static final IfcChangeActionEnum DEFAULT_CHANGE_ACTION_TYPE = IfcChangeActionEnum.NOCHANGE;
	public static final String UNKNOWN_APPLICATION_NAME = "Unknown";
	private static final String UNKNOWN_ORGANIZATION_NAME = "Unknown Organization";
	private long defaultApplicationOid;
	private OidProvider cobieOidProvider;
	private long defaultOwnerHistoryOid;
	private COBieIfcModel model;
	private long organizationOid;
	private int transformTimestamp;
	
	public OwnerHistoryHandler(COBieIfcModel model,OidProvider cobieOidProvider)
	{
		this.model = model;
		this.cobieOidProvider = cobieOidProvider;
		init();
	}
	
	public IfcOwnerHistory DefaultOwnerHistory()
	{
		return (IfcOwnerHistory) model.get(this.defaultOwnerHistoryOid);
	}
	
	public int ifcTimestampFromCalendar(Calendar calendar)
	{
		int timeIfc;
		if(calendar==null)
			timeIfc = transformTimestamp;
		else
		{
			double timeIfcDouble = Math.ceil((double)calendar.getTimeInMillis() / (double)1000);
			try
			{
				timeIfc = (int) timeIfcDouble;
			}
			catch(Exception ex)
			{
				timeIfc = transformTimestamp;
			}
		}
		
		return timeIfc;
	}
	
	
	private boolean ownerHistoryHasApplication(IfcOwnerHistory ownerHistory, String applicationName)
	{
		boolean hasApplication = false;
		if(ownerHistory!=null)
		{
			if(ownerHistory.getOwningApplication().getApplicationFullName().equalsIgnoreCase(applicationName))
				hasApplication=true;
		}
		return hasApplication;
	}
	
	private boolean ownerHistoryMatchesTimestamp(Calendar timestamp,IfcOwnerHistory ownerHistory)
	{
		boolean matches = false;
		int timeIfc;
		if (timestamp != null && ownerHistory!=null)
		{
			timeIfc = ifcTimestampFromCalendar(timestamp);
			matches = (timeIfc==ownerHistory.getCreationDate());
		}
		return matches;
	}
	
	public IfcOwnerHistory ownerHistoryFromEmailTimestampAndApplication(String email,
			Calendar timestamp, String applicationName)
	{
		
		IfcOwnerHistory ownerHistory =
				model.getOwnerHistory(email);
		IfcPersonAndOrganization personOrg =
				model.getContact(email);
		IfcApplication application =
				model.getExtSystem(applicationName);
		int timeIfc;
		boolean matchesTimestamp = ownerHistoryMatchesTimestamp(timestamp,ownerHistory);
		boolean matchesApplication = ownerHistoryHasApplication(ownerHistory,applicationName);
		if(ownerHistory==null || !matchesTimestamp || !matchesApplication)
		{
			timeIfc = ifcTimestampFromCalendar(timestamp);	
			ownerHistory = Ifc2x3tc1Factory.eINSTANCE
			.createIfcOwnerHistory();
			ownerHistory.setChangeAction(DEFAULT_CHANGE_ACTION_TYPE);
			if(application==null)
				ownerHistory.setOwningApplication(getApplicationFromName(applicationName));
			else
				ownerHistory.setOwningApplication(application);
			if(personOrg==null)
				ownerHistory.setOwningUser(DefaultOwnerHistory().getOwningUser());
			else
				ownerHistory.setOwningUser(personOrg);		
			ownerHistory.setCreationDate(timeIfc);
			model.add(ownerHistory, cobieOidProvider);
		}
		return ownerHistory;
	}
	
	public void contactToPersonOrgAndOwnerHistory(ContactType contact) throws Exception
	{
		String email;
		email = contact.getEmail();
		if (!model.containsContact(email))
		{
			contact.getCreatedBy();
			contact.getCreatedOn();
			IfcPerson person = ContactDeserializer.personFromContact(contact);
			IfcOrganization org = ContactDeserializer
					.organizationFromContact(contact);
			IfcTelecomAddress telecommAddress = ContactDeserializer
					.telecommAddressFromContact(contact);
			org.getAddresses().add(telecommAddress);
			IfcPersonAndOrganization personOrg = Ifc2x3tc1Factory.eINSTANCE
					.createIfcPersonAndOrganization();
			IfcActorRole actorRole = ContactDeserializer
					.actorRoleFromContact(contact);
			org.getRoles().add(actorRole);
			personOrg.setTheOrganization(org);
			personOrg.setThePerson(person);
			model.add(person, cobieOidProvider);
			model.add(telecommAddress, cobieOidProvider);
			model.add(actorRole, cobieOidProvider);
			model.add(org, cobieOidProvider);
			model.add(personOrg, cobieOidProvider);

		}
	}
	
	private String getCOBieOrganizationDescription()
	{
		return DeserializerStaticStrings.getCOBieOrgDescription();
	}
	
	private String getCOBieOrganizationId()
	{
		return DeserializerStaticStrings.getCOBieOrgId();
	}
	
	private String getCOBieOrganizationName()
	{
		return DeserializerStaticStrings.getCOBieOrgName();
	}
	
	private IfcOrganization createDefaultApplicationOrganization()
	{
		IfcOrganization organization = Ifc2x3tc1Factory.eINSTANCE
				.createIfcOrganization();
		organization.setName(getCOBieOrganizationName());
		organization.setId(getCOBieOrganizationId());
		organization.setDescription(getCOBieOrganizationDescription());
		return organization;
	}
	
	private IfcOrganization createOrganizationFromname(String organizationName)
	{
		IfcOrganization organization;
		if(COBieUtility.isNA(organizationName))
		{
			organization = (IfcOrganization) model.get(organizationOid);
			
		}
		else
		{
			organization = Ifc2x3tc1Factory.eINSTANCE
					.createIfcOrganization();
			
			organization.setName(organizationName);
			organization.setId(organizationName);
			organization.setDescription(organizationName);
			model.add(organization,cobieOidProvider);
		}
			
		return organization;
	}
	
	private void init()
	{		
		initializeDefaultApplicationAndOrganization();
		initializeDefaultOwnerHistory();
		Date tmpDate = new Date();
		transformTimestamp = ifcTimestampFromCalendar(COBieUtility.getDefaultCalendar());
	}
	
	private IfcApplication getApplicationFromName(String applicationName)
	{
		IfcApplication application;
		if(model.containsApplication(applicationName))
		{
			application = (IfcApplication) model.get(model.getApplicationOid(applicationName));
		}
		else
		{
			String tmpApplicationName = applicationName;
			String tmpOrgName;
			if(COBieUtility.isNA(applicationName))
			{
				tmpApplicationName = UNKNOWN_APPLICATION_NAME;
				tmpOrgName = UNKNOWN_ORGANIZATION_NAME;
			}
			else
			{
				tmpOrgName = APPLICATION_ORGANIZATION_PREFIX+applicationName;
			}
			IfcOrganization organization = createOrganizationFromname(tmpOrgName);
			application = applicationFromNameAndOrganization(tmpApplicationName,organization);
		}
		return application;
	}

	private IfcApplication applicationFromNameAndOrganization(String applicationName,
			IfcOrganization developerOrganization)
	{
		IfcApplication application;
		if(COBieUtility.isNA(applicationName))
		{
			application = (IfcApplication) model.get(defaultApplicationOid);
		}
		else
		{
			application = Ifc2x3tc1Factory.eINSTANCE
					.createIfcApplication();
			application.setApplicationFullName(applicationName);
			application.setApplicationIdentifier(applicationName);
			application.setApplicationDeveloper(developerOrganization);
			application.setVersion(COBieUtility.COBieNA);
			model.add(application,cobieOidProvider);
		}
		
		return application;
	}
	private void initializeDefaultApplicationAndOrganization()
	{
		IfcOrganization tmpOrg =
				createDefaultApplicationOrganization();
		IfcApplication application = Ifc2x3tc1Factory.eINSTANCE
				.createIfcApplication();
		application.setApplicationFullName(DeserializerStaticStrings.getDefaultApplicationName());
		application.setApplicationIdentifier(DeserializerStaticStrings.getDefaultApplicationName());
		application.setApplicationDeveloper(tmpOrg);
		application.setVersion(COBieUtility.COBieNA);
		IfcTelecomAddress emailAndPhone = Ifc2x3tc1Factory.eINSTANCE
				.createIfcTelecomAddress();
		emailAndPhone.getElectronicMailAddresses().add(
				DeserializerStaticStrings.getDefaultEmail());
		emailAndPhone.getTelephoneNumbers().add(
				DeserializerStaticStrings.getDefaultPhone());
		emailAndPhone
				.setWWWHomePageURL(DeserializerStaticStrings.getDefaultWWW());
		model.add(emailAndPhone, cobieOidProvider);
		tmpOrg.getAddresses().add(emailAndPhone);
		IfcPostalAddress postalAddress = Ifc2x3tc1Factory.eINSTANCE
				.createIfcPostalAddress();
		postalAddress
				.setCountry(DeserializerStaticStrings.getDefaultOrgCountry());
		postalAddress.setPostalBox(DeserializerStaticStrings
				.getDefaultOrgPostalBox());
		postalAddress.setRegion(DeserializerStaticStrings.getDefaultState());
		postalAddress.setPostalCode(DeserializerStaticStrings
				.getDefaultPostalCode());
		model.add(postalAddress, cobieOidProvider);
		tmpOrg.getAddresses().add(postalAddress);
		this.organizationOid = model.add(tmpOrg, cobieOidProvider);
		this.defaultApplicationOid = model.add(application, cobieOidProvider);
	}
	
	private void initializeDefaultOwnerHistory()
	{
		long tmpOid;
		IfcOwnerHistory ownerHistory = Ifc2x3tc1Factory.eINSTANCE
				.createIfcOwnerHistory();
		IfcOrganization theOrg = (IfcOrganization) model.get(organizationOid);
		IfcApplication theApplication = (IfcApplication) model
				.get(defaultApplicationOid);
		IfcPersonAndOrganization personOrg = Ifc2x3tc1Factory.eINSTANCE
				.createIfcPersonAndOrganization();
		IfcPerson person = Ifc2x3tc1Factory.eINSTANCE.createIfcPerson();
		person.setFamilyName(theOrg.getName());
		person.setGivenName(theOrg.getName());
		tmpOid = model.add(person, cobieOidProvider);
		personOrg.setTheOrganization(theOrg);
		personOrg.setThePerson((IfcPerson) model.get(tmpOid));
		tmpOid = model.add(personOrg, this.cobieOidProvider);
		ownerHistory.setOwningApplication(theApplication);
		ownerHistory.setOwningUser((IfcPersonAndOrganization) model.get(tmpOid));
		ownerHistory.setCreationDate(transformTimestamp);
		ownerHistory.setChangeAction(DEFAULT_CHANGE_ACTION_TYPE);
		this.defaultOwnerHistoryOid = model.add(ownerHistory, cobieOidProvider);

	}
	
	
	
	
	
}
