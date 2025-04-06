package entity;

import java.util.List;

public class AdditionalService {
	private String dodatnaUsluga;
	public AdditionalService(String dodatnaUsluga) {
		this.dodatnaUsluga = dodatnaUsluga;

	}
	public String getDodatnaUsluga() {
		return dodatnaUsluga;
	}
	public void setDodatnaUsluga(String dodatnaUsluga) {
		this.dodatnaUsluga = dodatnaUsluga;
	}
	public String toFileString() {
		return dodatnaUsluga;
	}

	public int compareTo(AdditionalService additionalService) {
		return this.getDodatnaUsluga().compareTo(additionalService.getDodatnaUsluga());
	}

  @Override
  public String toString() {
    return dodatnaUsluga;
  }
}
