/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.dynamicdatamapping.service;

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Eduardo Garcia
 */
public class BaseDDMServiceTestCase {

	protected DDMTemplate addDetailTemplate(long classPK, String name)
		throws Exception {

		String language = "xsd";

		return addTemplate(
			PortalUtil.getClassNameId(DDMStructure.class), classPK, name,
			DDMTemplateConstants.TEMPLATE_TYPE_DETAIL,
			DDMTemplateConstants.TEMPLATE_MODE_CREATE, language,
			getTestTemplateScript(language));
	}

	protected DDMTemplate addListTemplate(
			long classNameId, long classPK, String name)
		throws Exception {

		String language = DDMTemplateConstants.LANG_TYPE_VM;

		return addTemplate(
			classNameId, classPK, name, DDMTemplateConstants.TEMPLATE_TYPE_LIST,
			StringPool.BLANK, language, getTestTemplateScript(language));
	}

	protected DDMTemplate addListTemplate(long classPK, String name)
		throws Exception {

		return addListTemplate(
			PortalUtil.getClassNameId(DDMStructure.class), classPK, name);
	}

	protected DDMStructure addStructure(long classNameId, String name)
		throws Exception {

		String storageType = StorageType.XML.getValue();

		return addStructure(
			classNameId, null, name, getTestStructureXsd(storageType),
			storageType, DDMStructureConstants.TYPE_DEFAULT);
	}

	protected DDMStructure addStructure(
			long classNameId, String structureKey, String name, String xsd,
			String storageType, int type)
		throws Exception {

		return DDMStructureLocalServiceUtil.addStructure(
			TestPropsValues.getUserId(), TestPropsValues.getGroupId(),
			DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID, classNameId,
			structureKey, getDefaultLocaleMap(name), null, xsd, storageType,
			type, ServiceTestUtil.getServiceContext());
	}

	protected DDMTemplate addTemplate(
			long classNameId, long classPK, String name, String type,
			String mode, String language, String script)
		throws Exception {

		return addTemplate(
			classNameId, classPK,  null, name, type, mode, language, script);
	}

	protected DDMTemplate addTemplate(
			long classNameId, long classPK, String templateKey, String name,
			String type, String mode, String language, String script)
		throws Exception {

		return DDMTemplateLocalServiceUtil.addTemplate(
			TestPropsValues.getUserId(), TestPropsValues.getGroupId(),
			classNameId, classPK, templateKey, getDefaultLocaleMap(name), null,
			type, mode, language, script, ServiceTestUtil.getServiceContext());
	}

	protected Map<Locale, String> getDefaultLocaleMap(String defaultValue) {
		Map<Locale, String> map = new HashMap<Locale, String>();

		map.put(LocaleUtil.getDefault(), defaultValue);

		return map;
	}

	protected String getTestStructureXsd(String storageType) throws Exception {
		String text = StringPool.BLANK;

		if (storageType.equals(StorageType.XML.getValue())) {
			text = readText("test-structure.xsd");
		}

		return text;
	}

	protected String getTestTemplateScript(String language) throws Exception {
		String text = StringPool.BLANK;

		if (language.equals(DDMTemplateConstants.LANG_TYPE_VM)) {
			text = "#set ($preferences = $renderRequest.getPreferences())";
		}
		else if (language.equals("xsd")) {
			text = readText("test-template.xsd");
		}

		return text;
	}

	protected String readText(String fileName) throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + fileName);

		return StringUtil.read(inputStream);
	}

}