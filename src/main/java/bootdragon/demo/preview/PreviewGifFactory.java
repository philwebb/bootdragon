/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bootdragon.demo.preview;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTranscoder;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Component;

import bootdragon.demo.DemoRequest;
import bootdragon.demo.sourcecode.DemoSourceCode;

/**
 * Factory to create a {@link PreviewGif}. Creates an animated GIF by adding standard
 * frames showing 'spring run' followed by a single customized frame showing the result in
 * a web browser.
 *
 * @author Phillip Webb
 */
@Component
public class PreviewGifFactory {

	/**
	 * Create a preview GIF for the specified request and source.
	 * @param request the reequest
	 * @param sourceCode the source code
	 * @return a preview GIF
	 * @throws IOException
	 */
	public PreviewGif createPreview(DemoRequest request, DemoSourceCode sourceCode)
			throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream = ImageIO
				.createImageOutputStream(outputStream);
		try {
			ImageWriter writer = getWriter(imageOutputStream);
			write(writer, request.getMessage());
		}
		finally {
			imageOutputStream.close();
		}
		return new PreviewGif(outputStream.toByteArray(), TimeUnit.SECONDS.toMillis(14));
	}

	private void write(ImageWriter writer, String message) throws IOException {
		ImageReader reader = getReader(PreviewGifFactory.class
				.getResourceAsStream("run.gif"));
		writer.prepareWriteSequence(null);
		ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
		writeInitialFrames(reader, writer, imageWriteParam);
		writeResultFrame(reader.getImageMetadata(0), writer, imageWriteParam, message);
		writer.endWriteSequence();
	}

	private void writeInitialFrames(ImageReader reader, ImageWriter writer,
			ImageWriteParam imageWriteParam) throws IOException {
		int numberOfFrames = reader.getNumImages(true);
		for (int i = 0; i < numberOfFrames; i++) {
			BufferedImage image = reader.read(i);
			IIOMetadata metadata = reader.getImageMetadata(i);
			writer.writeToSequence(new IIOImage(image, null, metadata), imageWriteParam);
		}
	}

	private void writeResultFrame(IIOMetadata imageMetadata, ImageWriter writer,
			ImageWriteParam imageWriteParam, String message) throws IOException {
		ImageReader reader = getReader(getClass().getResourceAsStream("webbrowser.gif"));
		BufferedImage image = reader.read(0);
		renderMessageOverlay(image, message);
		ImageTypeSpecifier imageType = reader.getImageTypes(0).next();
		IIOMetadata metadata = getResultFrameMetadata(imageMetadata, imageType, writer);
		writer.writeToSequence(new IIOImage(image, null, metadata), imageWriteParam);
	}

	private IIOMetadata getResultFrameMetadata(IIOMetadata sourceMetadata,
			ImageTypeSpecifier sourceType, ImageTranscoder transcoder)
			throws IIOInvalidTreeException {
		IIOMetadata metadata = transcoder.convertImageMetadata(sourceMetadata,
				sourceType, null);
		String formatName = metadata.getNativeMetadataFormatName();
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(formatName);
		IIOMetadataNode graphicsControlExtension = getNode(root,
				"GraphicControlExtension");
		graphicsControlExtension.setAttribute("delayTime", "2000");
		metadata.setFromTree(formatName, root);
		return metadata;
	}

	private void renderMessageOverlay(BufferedImage image, String message) {
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Serif", Font.PLAIN, 48));
		FontMetrics metrics = graphics.getFontMetrics();
		int lineHeight = metrics.getHeight() + 8;
		int x = 30;
		int y = 100 + lineHeight;
		String[] lines = message.replace("\t", "\t\t").split("[\n\r]");
		for (String line : lines) {
			String[] words = line.split(" ");
			int i = 0;
			while (i < words.length) {
				String output = words[i++];
				while (i < words.length
						&& (x + metrics.stringWidth(output + " " + words[i]) < image
								.getWidth())) {
					output += " " + words[i++];
				}
				graphics.drawString(output, x, y);
				y = y + lineHeight;
			}
		}
		graphics.dispose();
	}

	private IIOMetadataNode getNode(IIOMetadataNode node, String name) {
		int nNodes = node.getLength();
		for (int i = 0; i < nNodes; i++) {
			if (node.item(i).getNodeName().equalsIgnoreCase(name)) {
				return ((IIOMetadataNode) node.item(i));
			}
		}
		IIOMetadataNode child = new IIOMetadataNode(name);
		node.appendChild(child);
		return child;
	}

	private ImageReader getReader(InputStream inputStream) throws IOException {
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		reader.setInput(ImageIO.createImageInputStream(inputStream));
		return reader;
	}

	private ImageWriter getWriter(ImageOutputStream outputStream) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
		writer.setOutput(outputStream);
		return writer;
	}

}
