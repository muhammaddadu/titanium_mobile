/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package org.appcelerator.titanium.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.appcelerator.titanium.config.TitaniumConfig;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class TitaniumFileHelper
{
	private static final String LCAT = "TiFileHlpr";
	private static final boolean DBG = TitaniumConfig.LOGD;

	public static final String TI_DIR = "tiapp";
	public static final String TI_DIR_JS = "tijs";
	private static final String MACOSX_PREFIX = "__MACOSX";
	private static final String TI_RESOURCE_PREFIX = "ti:";

	public static final String RESOURCE_ROOT_ASSETS = "file:///android_asset/Resources";

	static HashMap<String, Integer> systemIcons;

	private SoftReference<Context> softContext;
	private TitaniumNinePatchHelper nph;

	public TitaniumFileHelper(Context context)
	{
		softContext = new SoftReference<Context>(context);
		this.nph = new TitaniumNinePatchHelper();

		synchronized(TI_DIR) {
			if (systemIcons == null) {
				systemIcons = new HashMap<String, Integer>();
				systemIcons.put("ic_menu_camera", android.R.drawable.ic_menu_camera);
				//systemIcons.put("ic_menu_compose", android.R.drawable.ic_menu_compose);
				systemIcons.put("ic_menu_search", android.R.drawable.ic_menu_search);
				systemIcons.put("ic_menu_add", android.R.drawable.ic_menu_add);
				systemIcons.put("ic_menu_delete", android.R.drawable.ic_menu_delete);
				//systemIcons.put("ic_menu_archive", android.R.drawable.ic_menu_archive);
				//systemIcons.put("ic_menu_stop", android.R.drawable.ic_menu_stop);
				//systemIcons.put("ic_menu_refresh", android.R.drawable.ic_menu_refresh);
				systemIcons.put("ic_media_play", android.R.drawable.ic_media_play);
				systemIcons.put("ic_media_ff", android.R.drawable.ic_media_ff);
				systemIcons.put("ic_media_pause", android.R.drawable.ic_media_pause);
				systemIcons.put("ic_media_rew", android.R.drawable.ic_media_rew);
				systemIcons.put("ic_menu_edit", android.R.drawable.ic_menu_edit);
				systemIcons.put("ic_menu_close_clear_cancel", android.R.drawable.ic_menu_close_clear_cancel);
				systemIcons.put("ic_menu_save", android.R.drawable.ic_menu_save);
				//systemIcons.put("ic_menu_mark", android.R.drawable.ic_menu_mark);
				//systemIcons.put("ic_menu_back", android.R.drawable.ic_menu_back);
				//systemIcons.put("ic_menu_forward", android.R.drawable.ic_menu_forward);
				systemIcons.put("ic_menu_help", android.R.drawable.ic_menu_help);
				//systemIcons.put("ic_menu_home", android.R.drawable.ic_menu_home);
				systemIcons.put("ic_media_next", android.R.drawable.ic_media_next);
				systemIcons.put("ic_menu_preferences", android.R.drawable.ic_menu_preferences);
				systemIcons.put("ic_media_previous", android.R.drawable.ic_media_previous);
				systemIcons.put("ic_menu_revert", android.R.drawable.ic_menu_revert);
				systemIcons.put("ic_menu_send", android.R.drawable.ic_menu_send);
				systemIcons.put("ic_menu_share", android.R.drawable.ic_menu_share);
				systemIcons.put("ic_menu_view", android.R.drawable.ic_menu_view);
				systemIcons.put("ic_menu_zoom", android.R.drawable.ic_menu_zoom);
			}
		}
	}

	public InputStream openInputStream(String path, boolean report)
		throws IOException
	{
		InputStream is = null;

		Context context = softContext.get();
		if (context != null) {
			if (isTitaniumResource(path)) {
				String[] parts = path.split(":");
				if (parts.length != 3) {
					Log.w(LCAT, "malformed titanium resource url, resource not loaded: " + path);
					return null;
				}
				@SuppressWarnings("unused")
				String titanium = parts[0];
				String section = parts[1];
				String resid = parts[2];

				if (TI_RESOURCE_PREFIX.equals(section)) {
					is = TitaniumFileHelper.class.getResourceAsStream("/org/appcelerator/titanium/res/drawable/" + resid + ".png");
				} else if ("Sys".equals(section)) {
					Integer id = systemIcons.get(resid);
					if (id != null) {
						is = Resources.getSystem().openRawResource(id);
					} else {
						Log.w(LCAT, "Drawable not found for system id: " + path);
					}
				} else {
					Log.e(LCAT, "Unknown section identifier: " + section);
				}
			} else {
				path = joinPaths("Resources", path);
				is = context.getAssets().open(path);
			}
		}

		return is;
	}

	public Drawable loadDrawable(String path, boolean report) {
		return loadDrawable(path, report, false);
	}

	public Drawable loadDrawable(String path, boolean report, boolean checkForNinePatch)
	{
		Drawable d = null;
		InputStream is = null;
		try
		{
			if (checkForNinePatch) {
				if (path.endsWith(".png")) {
					if (!path.endsWith(".9.png")) {
						String apath = null;
						// First See if it's in the root dir
						apath = path.substring(0, path.lastIndexOf(".")) + ".9.png";
						try {
							is = openInputStream(apath, false);
							if (is != null) {
								path = apath;
							}
						} catch (IOException e) {
							if (DBG) {
								Log.d(LCAT, "path not found: " + apath);
							}
							// Let's see if there is a 9.png in the android folder.
							int i = path.lastIndexOf("/");
							if (i > 0) {
								apath = path.substring(i) +
									"android" +
									path.substring(i, path.lastIndexOf(".")) +
									".9.png";
							} else {
								apath = "android/" + path.substring(0, path.lastIndexOf(".")) + ".9.png";
							}

							try {
								is = openInputStream(apath, false);
								if (is != null) {
									path = apath;
								}
							} catch (IOException e1) {
								if (DBG) {
									Log.d(LCAT, "path not found: " + apath);
								}
							}
						}
					}
				}
				if (is == null) {
					is = openInputStream(path, report);
				}
				d = nph.process(Drawable.createFromStream(is, path));
			} else {
				is = openInputStream(path, report);
				d = Drawable.createFromStream(is, path);
			}
		} catch (IOException e) {
			Log.i(LCAT, path + " not found.");
			if (report) {
				Context context = softContext.get().getApplicationContext();
				if (context != null) {
					TitaniumUIHelper.doOkDialog(context, "Image Not Found", path, null);
				}
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					//Ignore
				}
			}
		}

		return d;
	}

	public boolean isTitaniumResource(String s) {
		boolean result = false;
		if (s != null && s.startsWith(TI_RESOURCE_PREFIX)) {
			result = true;
		}

		return result;
	}
/*
	public Drawable getTitaniumResource(Context context, String s) {
		Drawable d = null;

		if (isTitaniumResource(s)) {

			String[] parts = s.split(":");
			if (parts.length != 2) {
				Log.w(LCAT, "malformed titanium resource url, resource not loaded: " + s);
				return null;
			}
			String section = parts[0];
			String resid = parts[1];

			if (TI_RESOURCE_PREFIX.equals(section)) {
				InputStream is = null;
				try {
					is = TitaniumFileHelper.class.getResourceAsStream("/org/appcelerator/titanium/res/drawable/" + resid + ".png");
					d = new BitmapDrawable(is);
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// Ignore
						}
					}
				}
			} else if ("Sys".equals(section)) {
				Integer id = systemIcons.get(resid);
				if (id != null) {
					d = Resources.getSystem().getDrawable(id);
				} else {
					Log.w(LCAT, "Drawable not found for system id: " + s);
				}
			} else {
				Log.e(LCAT, "Unknown section identifier: " + section);
			}

		} else {
			Log.w(LCAT, "Ignoring non titanium resource string id: " + s);
		}

		return d;
	}
*/
	public String getResourceUrl(String path)
	{
        return joinPaths(RESOURCE_ROOT_ASSETS, path);
	}

	public String joinPaths(String pre, String post) {
		StringBuilder sb = new StringBuilder();
		sb.append(pre);
		if (pre.endsWith("/") && !post.startsWith("/")) {
			sb.append(post);
		} else if (!pre.endsWith("/") && post.startsWith("/")) {
			sb.append(post);
		} else if (!pre.endsWith("/") && !post.startsWith("/")) {
			sb.append("/").append(post);
		} else {
			sb.append(post.substring(1));
		}
		return sb.toString();
	}

	public void deployFromAssets(File dest)
		throws IOException
	{
		Context ctx = softContext.get();
		if (ctx != null) {
			ArrayList<String> paths = new ArrayList<String>();
			AssetManager am = ctx.getAssets();
			walkAssets(am, "", paths);

			// TODO clean old dir
			wipeDirectoryTree(dest);

			// copy from assets to dest dir
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			byte[] buf = new byte[8096];
			try {
				int len = paths.size();
				for(int i = 0; i < len; i++) {
					String path = paths.get(i);
					File f = new File(path);
					if(f.getName().indexOf(".") > -1) {
						bis = new BufferedInputStream(am.open(path), 8096);
						File df = new File(dest, path);
						if (DBG) {
							Log.d(LCAT, "Copying to: " + df.getAbsolutePath());
						}
						fos = new FileOutputStream(df);

						int read = 0;
						while((read = bis.read(buf)) != -1) {
							fos.write(buf, 0, read);
						}

						bis.close();
						bis = null;
						fos.close();
						fos = null;
					} else {
						File d = new File(dest,path);
						Log.d(LCAT, "Creating directory: " + d.getAbsolutePath());
						d.mkdirs();
					}
				}
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						//Ignore
					}
					bis = null;
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						//Ignore
					}
					fos = null;
				}
			}
		}
	}

	public void deployFromZip(File fname, File dest)
		throws IOException
	{
		wipeDirectoryTree(dest);

		ZipInputStream zis = null;
		ZipEntry ze = null;
		byte[] buf = new byte[8096];

		try {
			// See if we need to strip off parent dir.
			zis = getZipInputStream(new FileInputStream(fname));
			String root = getRootDir(zis);
			int rootLen = root.length();
			zis.close();

			if (DBG) {
				Log.d(LCAT, "Zip file root: " + root);
			}

			// Process the file
			zis = getZipInputStream(new FileInputStream(fname));
			while((ze = zis.getNextEntry()) != null) {
				String name = ze.getName();
				if (name.startsWith(MACOSX_PREFIX)) {
					zis.closeEntry();
					continue;
				}

				name = name.substring(rootLen);

				if(name.length() > 0) {
					if (DBG) {
						Log.d(LCAT, "Extracting " + name);
					}
					if (ze.isDirectory()) {
						File d = new File(dest, name);
						d.mkdirs();
						if (DBG) {
							Log.d(LCAT, "Created directory " + d.toString());
						}
						d = null;
					} else {
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(new File(dest,name));
							int read = 0;
							while((read = zis.read(buf)) != -1) {
								fos.write(buf, 0, read);
							}
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (Throwable t) {
									//Ignore
								}
							}
						}
					}
				}

				zis.closeEntry();
			}
		} finally {
			if (zis != null) {
				try {
					zis.close();
				}catch (Throwable t) {
					//Ignore
				}
			}
		}
	}

	public void wipeDirectoryTree(File path)
	{
		TreeSet<String> dirs = new TreeSet<String>(new Comparator<String>(){

			public int compare(String o1, String o2) {
				return o1.compareTo(o2) * -1;
			}});

		wipeDirectoryTree(path, dirs);

		Iterator<String> d = dirs.iterator();
		while(d.hasNext()) {
			String fn = d.next();
			File f = new File(fn);
			if (DBG) {
				Log.d(LCAT, "Deleting Dir: " + f.getAbsolutePath());
			}
			f.delete();
		}
	}

	public File getTempFile(String suffix)
		throws IOException
	{
		File result = null;
		Context context = softContext.get();

		if(context != null) {
			result = getTempFile(context.getCacheDir(), suffix);
		}
		return result;
	}

	public File getTempFile(File dir, String suffix)
		throws IOException
	{
		File result = null;
		Context context = softContext.get();
		if (context != null) {
			result = File.createTempFile("tia", suffix, dir);
		}
		return result;
	}

	public File getDataDirectory(boolean privateStorage)
	{
		File f = null;
		Context context = softContext.get();
		if (context != null) {

			if (privateStorage)
			{
				f = context.getDir("appdata",0);
			}
			else
			{
				f = new File("/sdcard/" + context.getPackageName());
				if (!f.exists())
				{
					f.mkdirs();
				}
			}
		}
		return f;
	}
	private void wipeDirectoryTree(File path, SortedSet<String> dirs) {
		File[] files = path.listFiles();
		if (files != null) {
			int len = files.length;
			for (int i = 0; i < len; i++) {
				File f = files[i];
				if (f.isDirectory()) {
					dirs.add(f.getAbsolutePath());
					wipeDirectoryTree(f, dirs);
				} else {
					if (DBG) {
						Log.d(LCAT, "Deleting File: " + f.getAbsolutePath());
					}
					f.delete();
				}
			}
		}
	}

	private void walkAssets(AssetManager am, String path, ArrayList<String> paths) throws IOException
	{
		if (titaniumPath(path)) {
			String[] files = am.list(path);
			if (files.length > 0) {
				for(int i = 0; i < files.length; i++) {
					String newPath = files[i];
					String todo = path;
					if (path.length() > 0) {
						todo = todo + "/" + newPath;
					} else {
						todo = newPath;
					}
					if (titaniumPath(todo)) {
						//Log.e(LCAT, todo);
						paths.add(todo);
						walkAssets(am, todo, paths);
					}
				}
			}
		}
	}

	private boolean titaniumPath(String path) {
		return path == "" || path.equals("tiapp.xml") || path.startsWith("Resources");
	}

	private ZipInputStream getZipInputStream(InputStream is)
		throws FileNotFoundException, IOException
	{
		return new ZipInputStream(is);
	}

	private String getRootDir(ZipInputStream zis)
		throws FileNotFoundException, IOException
	{
		String root = "";

		ZipEntry ze = null;
		while((ze = zis.getNextEntry()) != null) {
			String name = ze.getName();
			zis.closeEntry();

			if (name.startsWith(MACOSX_PREFIX)) {
				continue;
			} else {
				if(name.indexOf("tiapp.xml") > -1) {
					String [] segments = name.split("\\/");
					if (segments.length == 2) {
						root = segments[0] + "/";
						break;
					} else if (segments.length == 1) {
						break;
					}
				}
			}
		}
		return root;
	}
}
