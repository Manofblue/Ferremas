CREATE OR REPLACE PROCEDURE sp_save_invt(
    IN p_id_producto INTEGER,
    IN p_id_sucursal INTEGER,
    IN p_stock INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Verificar si ya existe el registro
    IF EXISTS (
        SELECT 1 FROM inventario
        WHERE id_producto = p_id_producto AND id_sucursal = p_id_sucursal
    ) THEN
        -- Si existe, actualiza el stock
        UPDATE inventario
        SET stock = p_stock,set id_producto=p_id_producto
        WHERE id_producto = p_id_producto AND id_sucursal = p_id_sucursal;
    ELSE
        -- Si no existe, inserta un nuevo registro
        INSERT INTO inventario (id_producto, id_sucursal, stock)
        VALUES (p_id_producto, p_id_sucursal, p_stock);
    END IF;
END;
$$;
