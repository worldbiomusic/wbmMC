package com.wbm.plugin.util.data.yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wbm.plugin.Main;

public class YamlManager {
	/*
	 * yaml 관리 매니저
	 * 
	 * [사용법]
	 * 
	 * 1. YamlMember를 구현하는 클래스 작성
	 * 
	 * 2. registerMember()에 등록
	 * 
	 * 3. setData()로 넘어가는 FileConfiguration config를 멤버클래스에서 사용
	 * 
	 */
	private Map<YamlMember, FileConfiguration> members;
	private final File rootForlder;

//	public YamlManager() {
//		// 기본 plugins 폴더로 설정
//		this(Main.getInstance().getDataFolder());
//	}

	public YamlManager(File rootForlder) {
		this.rootForlder = rootForlder;
		this.members = new HashMap<YamlMember, FileConfiguration>();
	}

	private boolean checkMemberFileExist(YamlMember member) {
		for (YamlMember allMember : this.members.keySet()) {
			if (allMember.getFileName().equalsIgnoreCase(member.getFileName())) {
				return true;
			}
		}
		return false;
	}

	public boolean registerMember(YamlMember member) {
		// 멤버 등록과 동시에 데이터 분배
		if (this.checkMemberFileExist(member)) {
			// 같은 파일 등록 불가능
			return false;
		} else {
			// 파일 만들기
			this.makeFile(member);
			// config 로드
			this.loadConfig(member);
			// 멤버에게 분배
			this.distributeData(member);
			return true;
		}
	}

	private File getMemberFile(YamlMember member) {
		// 멤버 파일 반환
		return new File(this.rootForlder, member.getFileName());
	}

	public void loadConfig(YamlMember member) {
		// 멤버 파일을 새로운 FileConfiguration으로 등록(업데이트)
		FileConfiguration config = YamlConfiguration.loadConfiguration(this.getMemberFile(member));
		this.members.put(member, config);
	}

	public void reload(YamlMember member) {
		// 리로드되면 참조중인 config파일이 자동으로 업데이트 됨 (굳이 distribute에서 다시 받을 필요 없음)
		if (this.members.containsKey(member)) {
			FileConfiguration memberConfig = this.members.get(member);
			try {
				memberConfig.load(this.getMemberFile(member));
				this.distributeData(member);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void makeFile(YamlMember member) {
		// root 폴더 없으면 만들기
		if(!this.rootForlder.exists()) {
			this.rootForlder.mkdir();
		}
		
		String fileName = member.getFileName();
		
		// 경로가 /(File.separator)로 구분되어있으면 상위 폴더도 모두다 만들기
		List<String> folders = Arrays.asList(fileName.split("/"));
		File parentFolder = this.rootForlder;
		for (int i = 0; i < folders.size() - 1; i++) {
			File folder = new File(parentFolder, folders.get(i));
			if (!folder.exists()) {
				folder.mkdir();
			}
			parentFolder = folder;
		}

		// 파일 없으면 만들기
		File file = this.getMemberFile(member);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void unregisterMember(YamlMember member) {
		// 멤버 제거
		this.members.remove(member);
	}

	public void distributeAllData() {
		// 모든 멤버에게 데이터 분배
		this.getMemberList().forEach(member -> this.distributeData(member));
	}

	public void distributeData(YamlMember member) {
		// 멤버에게 데이터 분배
		FileConfiguration config = this.members.get(member);
		member.setData(this, config);
	}

	public void saveAllData() {
		// 모든 멤버 데이터 저장
		this.getMemberList().forEach(member -> this.save(member));
	}

	public void save(YamlMember member) {
		// 파일 저장
		FileConfiguration config = this.members.get(member);
		File memberFile = this.getMemberFile(member);
		try {
			config.save(memberFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<YamlMember> getMemberList() {
		return new ArrayList<YamlMember>(this.members.keySet());
	}
}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
