use timora;
INSERT INTO availability (
    company_id,
    supplier_id,
    start_date,
    end_date,
    day_of_week,
    start_time,
    end_time,
    recurrence_type,
    slot_duration_minutes,
    capacity,
    status,
    notes,
    created_at
) VALUES (
             1,
             1,
             '2026-07-01',
             '2026-12-31',
             'MONDAY',
             '09:00:00',
             '17:00:00',
             'WEEKLY',
             30,
             5,
             'ACTIVE',
             'Disponibilidad para citas de los lunes.',
             NOW()
         );