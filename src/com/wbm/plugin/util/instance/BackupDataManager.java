package com.wbm.plugin.util.instance;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BackupDataManager {
	private JavaPlugin javaPlugin;

	public BackupDataManager(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
	}

	public void saveBackupData() {
		// Ref:
		// https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
		String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd+H;mm;ss"));

		File originFile = this.javaPlugin.getDataFolder();
		String backupDataFolderString = this.javaPlugin.getDataFolder().getName() + "_backup";
		File backupDataFolder = new File(originFile.getParentFile(), backupDataFolderString);
		if (!backupDataFolder.exists()) {
			backupDataFolder.mkdir();
		}
		
		File backupFile = new File(backupDataFolder, nowTime);

		try {
			// copy files with directory structure
			FileUtils.copyDirectoryStructure(originFile, backupFile);
			
			// copy files without directory structure
//			FileUtils.copyDirectory(originFile, backupFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startSavingBackupDataTask(int delay) {
		new BukkitRunnable() {
			@Override
			public void run() {
				saveBackupData();
			}
		}.runTaskTimer(javaPlugin, delay, delay);
	}

	public JavaPlugin getJavaPlugin() {
		return this.javaPlugin;
	}
}
