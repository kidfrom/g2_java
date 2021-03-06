package com.example.springwebh2mybatis.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PostKaryawanDto {
  @NotNull
  private String nama;
  private String alamat;
  private String rt;
  private String rw;
  private String kecamatan;
  private String kelurahan;
  private String telepon;
  @NotNull
  private Date input_date;
  @NotNull
  private String input_by;
  private Date approve_date;
  private String approve_by;

  public String getNama() {
    return nama;
  }

  public String getAlamat() {
    return alamat;
  }

  public String getRt() {
    return rt;
  }

  public String getRw() {
    return rw;
  }

  public String getKecamatan() {
    return kecamatan;
  }

  public String getKelurahan() {
    return kelurahan;
  }

  public String getTelepon() {
    return telepon;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  public Date getInput_date() {
    return input_date;
  }

  public String getInput_by() {
    return input_by;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  public Date getApprove_date() {
    return approve_date;
  }

  public String getApprove_by() {
    return approve_by;
  }
}
