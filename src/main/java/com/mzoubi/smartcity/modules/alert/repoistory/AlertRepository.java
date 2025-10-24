package com.mzoubi.smartcity.modules.alert.repoistory;

import com.mzoubi.smartcity.modules.alert.dto.AlertDto;
import com.mzoubi.smartcity.modules.alert.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert,Long> {

    Page<Alert> findByCityId(Long cityId, PageRequest pageRequest);
    Page<Alert> findByResolvedFalse(PageRequest pageRequest);
    Page<Alert> findByResolvedTrue(PageRequest pageRequest);

}
