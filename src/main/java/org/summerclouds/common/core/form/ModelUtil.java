/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerclouds.common.core.form;

import java.io.IOException;
import java.util.Map;

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.node.JsonNodeBuilder;
import org.summerclouds.common.core.node.NodeList;
import org.summerclouds.common.core.tool.MJson;
import org.summerclouds.common.core.tool.MXml;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ModelUtil {

    private static Log log = Log.getLog(ModelUtil.class);

    public static DefRoot fromXml(Element xml) {
        DefRoot root = new DefRoot();

        NamedNodeMap attrList = xml.getAttributes();
        for (int i = 0; i < attrList.getLength(); i++) {
            Node attrXml = attrList.item(i);
            root.addAttribute(attrXml.getNodeName(), xml.getAttribute(attrXml.getNodeName()));
        }

        toConfig(root, xml);
        return root;
    }

    private static void toConfig(DefComponent node, Element xml) {
        for (Element element : MXml.getLocalElementIterator(xml)) {
            DefComponent nextNode = new DefComponent(element.getNodeName());

            NamedNodeMap attrList = element.getAttributes();
            for (int i = 0; i < attrList.getLength(); i++) {
                Node attrXml = attrList.item(i);
                nextNode.addAttribute(
                        attrXml.getNodeName(), element.getAttribute(attrXml.getNodeName()));
            }
            node.addDefinition(nextNode);

            toConfig(nextNode, element);
        }
    }

    public static Document toXml(INode model) {

        try {
            Document doc = MXml.createDocument();
            Element rootXml = doc.createElement("root");
            doc.appendChild(rootXml);

            toXml(model, rootXml);

            return doc;
        } catch (Throwable t) {
            log.d(t);
        }
        return null;
    }

    private static void toXml(INode node, Element xml) throws DOMException, MException {
        for (String key : node.getPropertyKeys()) xml.setAttribute(key, node.getString(key, ""));

        for (INode next : node.getObjects()) {
            Element nextXml = xml.getOwnerDocument().createElement(next.getName());
            xml.appendChild(nextXml);
            toXml(next, nextXml);
        }
    }

    public static ObjectNode toJson(INode model) {
        try {
            return new JsonNodeBuilder().writeToJsonNodeObject(model);
        } catch (Throwable t) {
            log.d(t);
        }
        return null;
    }

    public static DefRoot fromJson(String content) throws JsonProcessingException, IOException {
        JsonNode model = MJson.load(content);
        if (!(model instanceof ObjectNode)) throw new IOException("json is not an object");
        return fromJson((ObjectNode) model);
    }

    public static DefRoot fromJson(ObjectNode json) {
        DefRoot root = new DefRoot();
        fromJson(json, root);
        return root;
    }

    private static void fromJson(ObjectNode json, DefComponent root) {

        for (Map.Entry<String, JsonNode> field : M.iterate(json.fields())) {
            if (field.getValue().isValueNode()) {
                root.addAttribute(field.getKey(), field.getValue().asText());
                root.setString(field.getKey(), field.getValue().asText());
            } else if (field.getValue().isObject()) {
                DefComponent nextNode = new DefComponent(field.getKey());
                root.setObject(field.getKey(), nextNode);
                ObjectNode nextJson = (ObjectNode) field.getValue();
                fromJson(nextJson, nextNode);
            } else if (field.getValue().isArray()) {
                ArrayNode nextArray = (ArrayNode) field.getValue();
                NodeList array = root.createArray(field.getKey());
                for (JsonNode nextJson : nextArray) {
                    if (nextJson instanceof ObjectNode) {
                        DefComponent nextNode = new DefComponent(null);
                        array.add(nextNode);
                        fromJson((ObjectNode) nextJson, nextNode);
                    }
                }
            }
        }
    }
}
