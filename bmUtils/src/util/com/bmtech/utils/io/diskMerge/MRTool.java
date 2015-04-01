package com.bmtech.utils.io.diskMerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.bmtech.utils.Misc;

public class MRTool {

	/**
	 * 
	 * @param toFile
	 *            the file merge to
	 * @param recordClass
	 *            the class to seri/deseri the records
	 * @param cmp
	 *            the comparator to compare the mReords
	 * @param mlst
	 *            the fileList to be merge
	 * @throws Exception
	 */
	public static void mergeTo(File toFile, Class<?> recordClass,
			Comparator<MRecord> cmp, int mergeFactor, File... mlst)
			throws Exception {
		mergeTo(toFile, recordClass, mergeFactor, cmp, mlst);
	}

	/**
	 * 
	 * @param toFile
	 *            toFile the file merge to
	 * @param recordClass
	 *            recordClass the class to seri/deseri the records
	 * @param mergeFactor
	 *            maxFile to merge
	 * @param cmp
	 *            the comparator to compare the mReords
	 * @param mlst
	 *            mlst the fileList to be merge
	 * @throws Exception
	 */
	public static void mergeTo(File toFile, Class<?> recordClass,
			int mergeFactor, Comparator<MRecord> cmp, File... mlst)
			throws Exception {
		makeSureFileExists(toFile);
		if (mlst.length == 0) {
			return;
		}
		int grp = mlst.length / mergeFactor;
		if (0 != mlst.length % mergeFactor) {
			grp++;
		}
		if (grp > 1) {
			File tmpFiles[] = new File[grp];
			for (int x = 0; x < grp; x++) {
				File tmpFile = getTmpFile(".mrg");
				int start = x * mergeFactor;
				int end = start + mergeFactor;
				if (end > mlst.length) {
					end = mlst.length;
				}
				File tmplst[] = new File[end - start];
				System.arraycopy(mlst, start, tmplst, 0, end - start);
				mergeToInner(tmpFile, recordClass, cmp, tmplst);
				tmpFiles[x] = tmpFile;
			}
			mergeTo(toFile, recordClass, mergeFactor, cmp, tmpFiles);
			for (File x : tmpFiles) {
				x.delete();
			}

		} else {
			mergeToInner(toFile, recordClass, cmp, mlst);
		}

	}

	private static void mergeToInner(File toFile, Class<?> recordClass,
			Comparator<MRecord> cmp, File... files) throws Exception {
		MOut mout = new MOut(toFile);
		PeekableQueue[] mlst = new PeekableQueue[files.length];
		for (int x = 0; x < mlst.length; x++) {
			mlst[x] = new RecordReader(files[x], recordClass);
		}
		try {
			while (true) {
				MRecord mm = null, tmp;
				for (PeekableQueue mq : mlst) {
					tmp = mq.peek();
					if (tmp == null)
						continue;

					if (mm == null) {
						mm = tmp;
					} else {
						int ret = cmp.compare(mm, tmp);
						if (ret > 0) {
							mm = tmp;
						}
					}
				}
				if (mm == null) {
					break;
				}
				for (PeekableQueue mq : mlst) {
					tmp = mq.peek();
					if (tmp == null)
						continue;
					if (cmp.compare(mm, tmp) == 0) {
						mq.take();
						mout.offer(tmp);
					}
				}
			}
		} finally {
			mout.close();
			for (int x = 0; x < mlst.length; x++) {
				mlst[x].close();
			}
		}

	}

	public static int reduce(File toFile, File fromFile, Class<?> recordClass,
			MReduceSelect cmp) throws Exception {
		RecordReader q = new RecordReader(fromFile, recordClass);
		try {
			return reduce(toFile, q, cmp);
		} finally {
			q.close();
		}
	}

	/**
	 * 
	 * @param cmp
	 * @param q
	 * @param toFile
	 *            the file reduce to
	 * @throws Exception
	 */
	private static int reduce(File toFile, PeekableQueue q, MReduceSelect cmp)
			throws Exception {
		makeSureFileExists(toFile);
		MOut mout = new MOut(toFile);
		MRecord last = null;
		int ret = 0;
		try {
			while (true) {
				MRecord crt = q.take();
				if (crt == null) {
					if (last != null) {
						mout.offer(last);
						ret++;
					}
					break;
				}
				if (last == null) {
					last = crt;
				} else {
					if (cmp.equals(last, crt)) {
						last = cmp.select(last, crt);
					} else {
						mout.offer(last);
						last = crt;
						ret++;
					}
				}
			}
		} finally {
			mout.close();
		}
		return ret;
	}

	// public static void sortFile(File toFile, File toSort, Class<?>
	// recordClass,
	// Comparator<MRecord> comparator) throws Exception{
	// int bufferSize = 1024 * 256;
	// sortFile(toFile, toSort, recordClass, comparator, bufferSize);
	// }
	public static void sortFile(File toFile, File toSort, Class<?> recordClass,
			Comparator<MRecord> c, int bufferSize) throws Exception {
		PeekableQueue q = new RecordReader(toSort, recordClass);
		try {
			sortFile(toFile, q, c, bufferSize, recordClass);
		} finally {
			q.close();
		}
	}

	public static void sortFile(File toFile, PeekableQueue q,
			Class<?> recordClass, Comparator<MRecord> c, int bufferSize)
			throws Exception {
		try {
			sortFile(toFile, q, c, bufferSize, recordClass);
		} finally {
			q.close();
		}
	}

	/**
	 * 
	 * @param c
	 *            MRecord's comparator
	 * @param q
	 *            peekableQueue
	 * @param toFile
	 *            sort result
	 * @param bufferSize
	 * @param recordClass
	 *            record
	 * @throws Exception
	 */
	private static void sortFile(File toFile, PeekableQueue q,
			Comparator<MRecord> c, int bufferSize, Class<?> recordClass)
			throws Exception {
		makeSureFileExists(toFile);
		MRecord crt = null;
		ArrayList<MRecord> lst = new ArrayList<MRecord>();
		ArrayList<File> fls = new ArrayList<File>();
		while (true) {
			crt = q.take();
			if (crt == null) {
				if (lst.size() > 0) {
					Collections.sort(lst, c);
					File tmpFile = getTmpFile(".srt");
					writeLstToFile(lst, tmpFile);
					fls.add(tmpFile);
				}
				break;
			}
			lst.add(crt);
			if (lst.size() >= bufferSize) {
				Collections.sort(lst, c);
				File tmpFile = getTmpFile(".srt");
				writeLstToFile(lst, tmpFile);
				fls.add(tmpFile);
				lst.clear();
			}
		}

		File[] fs = new File[fls.size()];
		fls.toArray(fs);
		MRTool.mergeTo(toFile, recordClass, c, bufferSize, fs);
		for (File f : fls) {
			f.delete();
		}
	}

	private static File getTmpFile(String v) throws IOException {
		String str = System.getProperty("mr-tmpDir");
		File tmpBase;
		if (str == null) {
			tmpBase = new File("mr-tmp");
		} else {
			tmpBase = new File(str);
		}
		if (!tmpBase.exists()) {
			if (!tmpBase.mkdirs()) {
				throw new IOException("can not create file " + tmpBase);
			}
		}
		while (true) {
			int vi = Misc.randInt(0, 100000000);
			File ret = new File(tmpBase, "mrtmp" + vi + v);
			if (ret.exists()) {
				continue;
			} else {
				if (!ret.createNewFile()) {
					throw new IOException("can not create file " + tmpBase);
				}
				return ret;
			}
		}

	}

	private static void writeLstToFile(ArrayList<MRecord> lst, File file)
			throws IOException {
		MOut mo = new MOut(file);
		for (MRecord me : lst) {
			mo.offer(me);
		}
		mo.close();
	}

	public static void diff(File toFile, File from, File cmpFile,
			Class<?> recordClass, Comparator<MRecord> sucp) throws Exception {
		makeSureFileExists(toFile);
		RecordReader qFrom = new RecordReader(from, recordClass);
		RecordReader qToCompare = new RecordReader(cmpFile, recordClass);
		MOut out = new MOut(toFile);
		try {
			boolean qcmpIsEnd = false;
			while (true) {
				MRecord su = qFrom.take();
				if (su == null) {
					break;
				}
				if (qcmpIsEnd) {
					out.offer(su);
				} else {
					boolean find = false;
					while (true) {
						MRecord suTmp = qToCompare.peek();
						if (suTmp == null) {
							qcmpIsEnd = true;
							break;
						} else {
							int v = sucp.compare(su, suTmp);
							if (v == 0) {
								find = true;
								break;
							} else if (v < 0) {
								break;
							} else {
								qToCompare.take();
							}
						}
					}
					if (!find) {
						out.offer(su);
					}
				}
			}
		} finally {
			out.close();
			qFrom.close();
			qToCompare.close();
		}
	}

	public static void makeSureFileExists(File toFile) throws IOException {
		if (!toFile.exists()) {
			File par = toFile.getParentFile();
			if (!par.exists()) {
				if (!par.mkdirs()) {
					throw new IOException("can not create file " + par);
				}
			}

			if (!toFile.createNewFile()) {
				throw new IOException("can not create file " + toFile);
			}
		}
	}
}
