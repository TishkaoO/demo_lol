package com.example.demo_lol.service;

import com.example.demo_lol.dto.EmployeeDto;
import com.example.demo_lol.dto.TestEmpl;
import com.example.demo_lol.entity.Employee;
import com.example.demo_lol.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public List<Long> existsById(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> existIDs = new ArrayList<>();
        for (Long tmp : ids) {
            Optional<Employee> byId = repository.findById(tmp);
            if (!byId.isEmpty()) {
                existIDs.add(byId.get().getId());
            }
        }
        return existIDs;
    }

    public void update(final List<EmployeeDto> employeeDtos) {

        String sql = "UPDATE employee " +
                "SET id=?, name=?" +
                " WHERE id=?";


        List<Object[]> batchParams = new ArrayList<>();
        for (EmployeeDto obj : employeeDtos) {
            Object[] params = {
                    obj.getId(),
                    obj.getName(),
                    obj.getId()
            };
            batchParams.add(params);
        }
        jdbcTemplate.batchUpdate(sql, batchParams);
    }

    public void save(final List<EmployeeDto> employeeDtos) {
        String sql = "INSERT INTO employee(id, name)" +
                " VALUES( ?, ?)";
        List<Object[]> batchParams = new ArrayList<>();
        for (EmployeeDto obj : employeeDtos) {
            Object[] params = {
                    obj.getId(),
                    obj.getName()
            };
            batchParams.add(params);
        }
        jdbcTemplate.batchUpdate(sql, batchParams);
    }

    public void saveOrUpdate(TestEmpl empl) {
        List<Long> idList = empl.getEmployeeDtoList().stream()
                .map(EmployeeDto::getId)
                .collect(Collectors.toList());

        List<Long> forUpdate = existsById(idList);

        List<EmployeeDto> employeesToUpdate = empl.getEmployeeDtoList().stream()
                .filter(employee -> forUpdate.contains(employee.getId()))
                .collect(Collectors.toList());

        List<EmployeeDto> employeesToSave = empl.getEmployeeDtoList().stream()
                .filter(employee -> !forUpdate.contains(employee.getId()))
                .collect(Collectors.toList());


        update(employeesToUpdate);
        save(employeesToSave);
    }
}
