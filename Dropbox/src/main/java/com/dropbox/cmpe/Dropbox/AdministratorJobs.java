package com.dropbox.cmpe.Dropbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.dropbox.cmpe.Dropbox.domain.AmazonCommon;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;

@Every("5760mn")
public class AdministratorJobs extends Job {

	static int i = 0;

	@Override
	public void doJob() {

		Date date = new Date();
		BufferedWriter br = null;
		File newfile = new File("Backup.txt");
		if (!newfile.exists()) {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		try {
			FileWriter fileWritter = new FileWriter(newfile.getAbsoluteFile());
			br = new BufferedWriter(fileWritter);
			br.write(date.toGMTString());
			br.write("hello file " + i + " data");

			AmazonCommon common = new AmazonCommon();
			AWSCredentials credentials = common.getCredentials();
			AmazonGlacierClient client = common.getClient(credentials);
			client.setEndpoint("https://glacier.us-west-1.amazonaws.com/");

			String marker = null;
			do {
				ListVaultsRequest request = new ListVaultsRequest().withLimit(
						"500").withMarker(marker);
				ListVaultsResult listVaultsResult = client.listVaults(request);
				List<DescribeVaultOutput> vaultList = listVaultsResult
						.getVaultList();
				marker = listVaultsResult.getMarker();

				for (DescribeVaultOutput vault : vaultList) {

					br.write(vault.getCreationDate() + "\n");
					br.write(vault.getLastInventoryDate() + "\n");
					br.write(vault.getNumberOfArchives() + "\n");
					br.write(vault.getSizeInBytes() + "\n");
					br.write(vault.getVaultARN() + "\n");
					br.write(vault.getVaultName() + "\n");
					br.write("\n\n\n");

					System.out.println("\nCreationDate: "
							+ vault.getCreationDate() + "\nLastInventoryDate: "
							+ vault.getLastInventoryDate()
							+ "\nNumberOfArchives: "
							+ vault.getNumberOfArchives() + "\nSizeInBytes: "
							+ vault.getSizeInBytes() + "\nVaultARN: "
							+ vault.getVaultARN() + "\nVaultName: "
							+ vault.getVaultName());
				}
			} while (marker != null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
