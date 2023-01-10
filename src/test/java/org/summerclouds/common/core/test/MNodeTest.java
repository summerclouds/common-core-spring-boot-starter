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
package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.error.NotFoundException;
import org.summerclouds.common.core.form.DefRoot;
import org.summerclouds.common.core.form.definition.FaShowInformationPanel;
import org.summerclouds.common.core.form.definition.FmText;
import org.summerclouds.common.core.form.definition.IFmElement;
import org.summerclouds.common.core.node.DefaultNodeFactory;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.node.MNode;
import org.summerclouds.common.core.node.MProperties;
import org.summerclouds.common.core.node.NodeList;
import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.tool.MFile;
import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.tool.MSystem;
import org.summerclouds.common.core.tool.MXml;
import org.summerclouds.common.junit.TestCase;
import org.w3c.dom.Document;

public class MNodeTest extends TestCase {

    @Test
    public void testPath() {

        MNode root = new MNode();
        {
            INode res = INode.findOrCreateNode(root, "nr1/nr2/nr3");
            String path = INode.getPath(res);
            System.out.println(path);
            assertEquals("/nr1/nr2/nr3", path);
        }
        {
            INode res = INode.findOrCreateNode(root, "nr1/a2[4]/nr3");
            String path = INode.getPath(res);
            System.out.println(path);
            assertEquals("/nr1/a2[4]/nr3", path);
        }
        {
            INode res = INode.findOrCreateNode(root, "/nr1/nr2/nr3");
            String path = INode.getPath(res);
            System.out.println(path);
            assertEquals("/nr1/nr2/nr3", path);
        }
        {
            INode res = INode.findOrCreateNode(root, "/nr1/a2[4]/nr3");
            String path = INode.getPath(res);
            System.out.println(path);
            assertEquals("/nr1/a2[4]/nr3", path);
        }
        {
            INode res = INode.findOrCreateNode(root, "/");
            String path = INode.getPath(res);
            System.out.println(path);
            assertEquals("/", path);
        }
        {
            INode res = INode.findOrCreateNode(root, "");
            String path = INode.getPath(res);
            System.out.println(path);
            assertEquals("/", path);
        }
    }

    @Test
    public void testPropertiesWithUTF8() throws IOException, NotFoundException {
        InputStream is = MSystem.locateResource(this, "utf8.properties").openStream();

        MProperties props = MProperties.load(is);
        System.out.println(props);
        assertTrue(props.containsKey("utf8key_u_\u2022"));
        assertTrue(props.containsKey("test1\u00b0"));
        //        assertTrue(props.containsKey("utf8key•"));
        assertEquals("360\u2022", props.getString("utf8key_u_\u2022"));
        assertEquals("test\u00b0", props.getString("test1\u00b0"));
        //        assertEquals("360\u2022", props.getString("utf8key\u2022"));
    }

    @Test
    public void testDefRootSerializable() throws MException, IOException, ClassNotFoundException {

        String serialized = null;
        {
            DefRoot root = new DefRoot(new FaShowInformationPanel(), new FmText("test1"));

            serialized = MCast.serializeToString(root);
        }

        {
            DefRoot root = (DefRoot) MCast.unserializeFromString(serialized, null);
            System.out.println(root);
            {
                assertEquals(true, root.getBoolean("showInformation"));
                NodeList array = root.getArray(IFmElement.ELEMENT_NODE);
                assertEquals(1, array.size());
                INode ele = array.get(0);
                assertEquals("text", ele.getString("type"));
                assertEquals("test1", ele.getString("name"));
            }
            root.build();
            assertTrue(root.isBuild());
            {
                assertEquals(true, root.getBoolean("showInformation"));
                NodeList array = root.getArray(IFmElement.ELEMENT_NODE);
                assertEquals(1, array.size());
                INode ele = array.get(0);
                assertEquals("text", ele.getString(IFmElement.TYPE));
                assertEquals("test1", ele.getString(IFmElement.NAME));
            }
        }
    }

    @Test
    public void testSerializable() throws MException, IOException, ClassNotFoundException {

        String serialized = null;

        {
            INode c = new MNode();
            c.setString("test1", "wow");
            c.setString("test2", "alf");

            INode c1 = new MNode();
            c1.setString("test1", "wow");
            c1.setString("test2", "alf");

            INode c2 = new MNode();
            c2.setString("test1", "wow");
            c2.setString("test2", "alf");

            INode c3 = new MNode();
            c3.setString("test1", "wow");
            c3.setString("test2", "alf");

            c.setObject("c1", c1);
            NodeList array = c.createArray("array");
            array.add(c2);
            array.add(c3);

            derTeschd(c, false);
            derTeschd(c1, false);
            derTeschd(c2, false);
            derTeschd(c3, false);

            serialized = MCast.serializeToString(c);
        }

        {
            INode c = (INode) MCast.unserializeFromString(serialized, null);
            derTeschd(c, false);

            INode c1 = c.getObject("c1");
            derTeschd(c1, false);

            NodeList array = c.getArray("array");
            INode c2 = array.get(0);
            derTeschd(c2, false);
            INode c3 = array.get(0);
            derTeschd(c3, false);
        }
    }

    @Test
    public void testProperties() throws MException {
        {
            INode c = new MNode();
            c.setString("test1", "wow");
            c.setString("test2", "alf");

            derTeschd(c, false);
        }
        {
            INode c = new MNode();
            c.setString("test1", "wow");
            c.setString("test2", "alf");

            // save
            File file = new File("target/config.properties");
            DefaultNodeFactory dcf = new DefaultNodeFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            System.out.println("---");
            System.out.println(MFile.readFile(file));
            System.out.println("---");
            // read
            INode c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, false);
        }
    }

    @Test
    public void testXml() throws Exception {

        String xml =
                "<start test1='wow' test2='alf'><sub test1='wow1' test2='alf1'/><sub test1='wow2' test2='alf2'/><sub test1='wow3' test2='alf3'/></start>";
        {
            Document doc = MXml.loadXml(xml);

            INode c = INode.readFromXmlString(doc.getDocumentElement());

            derTeschd(c, true);
        }
        {
            Document doc = MXml.loadXml(xml);
            INode c = INode.readFromXmlString(doc.getDocumentElement());

            // save
            File file = new File("target/config.xml");
            DefaultNodeFactory dcf = new DefaultNodeFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            // read
            INode c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, true);
        }
    }

    @Test
    public void testYaml() throws Exception {
        String yaml =
                "test1: wow\n"
                        + "test2: alf\n"
                        + "sub:\n"
                        + "- test1: wow1\n"
                        + "  test2: alf1\n"
                        + "- test1: wow2\n"
                        + "  test2: alf2\n"
                        + "- test1: wow3\n"
                        + "  test2: alf3\n"
                        + "projects:\n"
                        + "- properties:\n"
                        + "    name: name\n"
                        + "    url: http://test.de";

        {
            INode c = INode.readFromYamlString(yaml);
            derTeschd(c, true);
        }
        {
            INode c = INode.readFromYamlString(yaml);
            File file = new File("target/config.yaml");
            DefaultNodeFactory dcf = new DefaultNodeFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            // read
            INode c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, true);

            // check file
            String content = MFile.readFile(file);
            System.out.println(content);
            assertFalse(content.contains("{"));
        }
    }

    @Test
    public void testJsonStream() throws Exception {

        String json =
                MString.replaceAll(
                        "{'test1':'wow','test2':'alf','boolon':true,'booloff':false,"
                                + "'sub': [  "
                                + "{'test1':'wow1','test2':'alf1'} , "
                                + "{'test1':'wow2','test2':'alf2'} , "
                                + "{'test1':'wow3','test2':'alf3'}  "
                                + "] }",
                        "'",
                        "\"");
        {
            INode c = INode.readFromJsonString(json);
            derTeschd(c, true);
            assertTrue(c.getBoolean("boolon"));
            assertFalse(c.getBoolean("booloff"));
        }
        {
            INode c = INode.readFromJsonString(json);
            File file = new File("target/config.json");
            DefaultNodeFactory dcf = new DefaultNodeFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);
            System.out.println("File: " + MFile.readFile(file));
            // read
            INode c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, true);
            assertTrue(c.getBoolean("boolon"));
            assertFalse(c.getBoolean("booloff"));
        }
    }

    @Test
    public void testHash() throws Exception {

        INode c = new MNode();
        c.setString("test1", "wow");
        c.setString("test2", "alf");
        NodeList a = c.createArray("sub");
        INode s = a.createObject();
        s.setString("test1", "wow1");
        s.setString("test2", "alf1");
        s = a.createObject();
        s.setString("test1", "wow2");
        s.setString("test2", "alf2");
        s = a.createObject();
        s.setString("test1", "wow3");
        s.setString("test2", "alf3");

        derTeschd(c, true);
    }

    //    @Test
    //    public void testClone() throws Exception {
    //
    //        String xml =
    //                "<start test1='wow' test2='alf'><sub test1='wow1' test2='alf1'/><sub
    // test1='wow2' test2='alf2'/><sub test1='wow3' test2='alf3'/></start>";
    //        Document doc = MXml.loadXml(xml);
    //
    //        IConfig src = IConfig.createFromXml(doc.getDocumentElement());
    //
    //        IConfig tar1 = new IConfig();
    //        JsonConfig tar2 = new JsonConfig();
    //        XmlConfig tar3 = new XmlConfig();
    //
    //        builder.cloneConfig(src, tar1);
    //        builder.cloneConfig(src, tar2);
    //        builder.cloneConfig(src, tar3);
    //
    //        derTeschd(src, true);
    //        derTeschd(tar1, true);
    //        derTeschd(tar2, true);
    //        derTeschd(tar3, true);
    //    }

    @Test
    private void derTeschd(INode c, boolean testsub) throws MException {
        System.out.println(MSystem.findCallerMethod(3) + ": " + c);
        assertEquals("wow", c.getString("test1", "no"));
        assertEquals("alf", c.getString("test2", "no"));
        assertEquals("no", c.getString("test3", "no"));

        assertNull(c.getObjectOrNull("test4"));

        if (!testsub) return;

        // sub config tests

        Collection<INode> list = c.getArray("sub");
        assertEquals(3, list.size());

        Iterator<INode> listIter = list.iterator();
        INode sub = listIter.next();
        assertEquals("wow1", sub.getString("test1", "no"));
        assertEquals("alf1", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        sub = listIter.next();
        assertEquals("wow2", sub.getString("test1", "no"));
        assertEquals("alf2", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        sub = listIter.next();
        assertEquals("wow3", sub.getString("test1", "no"));
        assertEquals("alf3", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        // change properties

        c.setString("test1", "aloa");
        c.setString("test3", "nix");
        assertEquals("aloa", c.getString("test1", "no"));
        assertEquals("alf", c.getString("test2", "no"));
        assertEquals("nix", c.getString("test3", "no"));

        // change config

        sub = c.createObject("sub");
        sub.setString("test1", "aloa4");
        sub.setString("test2", "alf4");
        assertEquals("aloa4", sub.getString("test1", "no"));
        assertEquals("alf4", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        //		assertEquals( 2, c.moveConfig(sub, WritableResourceNode.MOVE_UP) );
        //		assertEquals( 3, c.moveConfig(sub, WritableResourceNode.MOVE_DOWN) );
        //		assertEquals( 0, c.moveConfig(sub, WritableResourceNode.MOVE_FIRST) );
        //		assertEquals( 3, c.moveConfig(sub, WritableResourceNode.MOVE_LAST) );

    }
}
