package com.atshuo.audit.dataPermission.dummy;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter @Getter
public class SysUser {

    private Long id;

    private Set<Long> entIdList;
}
