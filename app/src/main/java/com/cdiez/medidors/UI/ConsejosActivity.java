package com.cdiez.medidors.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cdiez.medidors.Adapters.ConsejosAdapter;
import com.cdiez.medidors.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConsejosActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] titles = getTitles();

        String[] consejos = getConsejos();

        ConsejosAdapter adapter = new ConsejosAdapter(titles, consejos);
        mRecyclerView.setAdapter(adapter);
    }

    @NonNull
    private String[] getConsejos() {
        return new String[]{"Utiliza la vegetación a tu favor; plantar árboles en puntos estratégicos " +
                    "ayuda a desviar las corrientes de aire frío en invierno y a generar sombras en el verano.\n\n" +
                    "Mediante la instalación de toldos de lona o aleros inclinados, persianas de aluminio," +
                    " vidrios polarizados, recubrimientos, mallas y películas plásticas, se evita que el" +
                    " sol llegue directamente al interior. Así se pueden obtener ahorros en el consumo de" +
                    " energía eléctrica por el uso de aire acondicionado.\n\nEl aislamiento adecuado de" +
                    " techos y paredes ayuda a mantener una temperatura agradable en la casa.\n" +
                    "Si utilizas unidades centrales de aire acondicionado, aísla también los ductos.\n\n" +
                    "Es relativamente sencillo sellar las ventanas y puertas de la casa con pasta de silicón," +
                    " para que no entre el frío en los meses de invierno y no se escape en los meses calurosos.\n\n" +
                    "Cuando compres o remplaces el equipo, verifica que sea el adecuado a tus necesidades.\n" +
                    "Dale mantenimiento periódico y limpia los filtros regularmente. Vigila el termostato," +
                    " puede significar un ahorro adicional de energía eléctrica si permanece a 18°C (65°F)" +
                    " en el invierno, y a 25°C (78°F) en verano.\n\nEn clima seco usa el cooler, es más" +
                    " económico y consume menos energía que el aire acondicionado.",

                    "Los filtros y los depósitos de polvo y basura de la aspiradora saturados hacen que" +
                            " el motor trabaje sobrecargado y reduzca su vida útil. Cámbialos cada vez que" +
                            " sea necesario.\n\nVerifica que la manguera y los accesorios estén en buen estado.",

                    "No dejes encendidas lámparas, radios, televisores u otros aparatos eléctricos cuando" +
                            " nadie los está utilizando.",

                    "Mantén siempre limpios de residuos el horno de microondas, el horno eléctrico y el" +
                            " tostador, así durarán más tiempo y consumirán menos energía.",

                    "Utiliza lámparas fluorescentes compactas en sustitución de focos incandescentes; " +
                            "éstas proporcionan el mismo nivel de iluminación, duran diez veces más y " +
                            "consumen cuatro veces menos energía eléctrica.\n\nPinta el interior de la " +
                            "casa con colores claros, la luz se refleja en ellos y requieres menos energía " +
                            "para iluminar.",

                    "Comprueba que la instalación eléctrica no tenga fugas. Para eso, desconecta todos " +
                            "los aparatos eléctricos, incluyendo relojes y timbre; apaga todas las luces " +
                            "y verifica que el disco del medidor no gire; si el disco sigue girando, " +
                            "manda revisar la instalación.",

                    "Carga la lavadora al máximo permisible cada vez, así disminuirá el número de " +
                            "sesiones de lavado semanal.\nUtiliza sólo el detergente necesario; el exceso " +
                            "produce mucha espuma y hace trabajar al motor más de lo conveniente.",

                    "Una licuadora que trabaja con facilidad dura más y gasta menos; comprueba que las " +
                            "aspas siempre tengan filo y no estén quebradas.",

                    "La plancha es otro aparato que consume mucha energía. Utilizarla de manera ordenada " +
                            "y programada, ahorra energía y reduce los gastos.\n\nPlancha la mayor cantidad " +
                            "posible de ropa en cada ocasión, dado que conectar muchas veces la plancha " +
                            "ocasiona más gasto de energía que mantenerla encendida por un rato.\n\n" +
                            "Plancha primero la ropa gruesa, o que necesite más calor, y deja para el final " +
                            "la delgada, que requiere menos calor; desconecta la plancha poco antes de " +
                            "terminar para aprovechar la temperatura acumulada.\n\nNo dejes la plancha " +
                            "conectada innecesariamente.\n\nRevisa la superficie de la plancha para que " +
                            "esté siempre tersa y limpia; así se transmitirá el calor de manera uniforme." +
                            "\n\nRevisa que el cable y la clavija estén en buenas condiciones.",

                    "El refrigerador es uno de los aparatos que consume más energía en el hogar.\n\n" +
                            "Sitúa el refrigerador alejado de la estufa y fuera del alcance de los rayos " +
                            "del sol. Comprueba que la puerta selle perfectamente y revisa periódicamente " +
                            "el empaque, si no cierra bien puede generar un consumo hasta tres veces " +
                            "mayor al normal.\nDeja enfriar los alimentos antes de refrigerarlos. La " +
                            "posición correcta del termostato es entre los números 2 y 3. En clima " +
                            "caluroso, entre los números 3 y 4.\n\n\n" +
                            "Si piensas comprar refrigerador nuevo, selecciona el que consuma menos energía " +
                            "eléctrica. Revisa la etiqueta de eficiencia energética, que indica que ese " +
                            "aparato cumple con la Norma Oficial Mexicana y ahorra energía. Recuerda que " +
                            "los de deshielo automático consumen 12% más de electricidad y eso significa " +
                            "mayor gasto.\n\n\n" +
                            "Descongela el refrigerador y limpia con un paño húmedo el cochambre que se " +
                            "acumula en la parte posterior, por lo menos cada dos meses. Limpia los tubos " +
                            "del condensador ubicados en la parte posterior o inferior del aparato por " +
                            "lo menos dos veces al año."};
    }

    @NonNull
    private String[] getTitles() {
        return new String[]{"Aire acondicionado y calefacción", "Aspiradora", "Audio y vídeo",
                    "Horno y tostador", "Iluminación", "Instalación eléctrica", "Lavadora", "Licuadora",
                    "Plancha", "Refrigerador"};
    }
}
