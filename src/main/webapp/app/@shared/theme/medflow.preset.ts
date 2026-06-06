import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const MedflowPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{teal.50}',
      100: '{teal.100}',
      200: '{teal.200}',
      300: '{teal.300}',
      400: '{teal.400}',
      500: '{teal.500}',
      600: '{teal.600}',
      700: '{teal.700}',
      800: '{teal.800}',
      900: '{teal.900}',
      950: '{teal.950}',
    },
    focusRing: {
      width: '2px',
      style: 'solid',
      color: '{primary.500}',
      offset: '2px',
      shadow: 'none',
    },
    formField: {
      borderRadius: '10px',
      paddingX: '0.875rem',
      paddingY: '0.625rem',
      sm: {
        paddingX: '0.75rem',
        paddingY: '0.5rem',
      },
      lg: {
        paddingX: '1rem',
        paddingY: '0.75rem',
      },
    },
    content: {
      borderRadius: '12px',
    },
    overlay: {
      select: {
        borderRadius: '12px',
      },
      popover: {
        borderRadius: '12px',
      },
      modal: {
        borderRadius: '12px',
      },
    },
    colorScheme: {
      light: {
        surface: {
          0: '#ffffff',
          50: '{slate.50}',
          100: '{slate.100}',
          200: '{slate.200}',
          300: '{slate.300}',
          400: '{slate.400}',
          500: '{slate.500}',
          600: '{slate.600}',
          700: '{slate.700}',
          800: '{slate.800}',
          900: '{slate.900}',
          950: '{slate.950}',
        },
        primary: {
          color: '{primary.600}',
          contrastColor: '#ffffff',
          hoverColor: '{primary.700}',
          activeColor: '{primary.800}',
        },
        highlight: {
          background: '{primary.600}',
          focusBackground: '{primary.700}',
          color: '#ffffff',
          focusColor: '#ffffff',
        },
        text: {
          color: '{surface.900}',
          hoverColor: '{surface.950}',
          mutedColor: '{surface.600}',
          hoverMutedColor: '{surface.700}',
        },
        content: {
          background: '{surface.0}',
          hoverBackground: '{surface.50}',
          borderColor: '{surface.200}',
          color: '{text.color}',
          hoverColor: '{text.hoverColor}',
        },
        formField: {
          background: '{surface.0}',
          disabledBackground: '{surface.100}',
          filledBackground: '{surface.50}',
          filledHoverBackground: '{surface.50}',
          filledFocusBackground: '{surface.0}',
          borderColor: '{surface.300}',
          hoverBorderColor: '{surface.400}',
          focusBorderColor: '{primary.color}',
          invalidBorderColor: '{red.500}',
          color: '{surface.900}',
          disabledColor: '{surface.500}',
          placeholderColor: '{surface.400}',
          invalidPlaceholderColor: '{red.500}',
          floatLabelColor: '{surface.500}',
          floatLabelFocusColor: '{primary.color}',
          floatLabelActiveColor: '{surface.500}',
          floatLabelInvalidColor: '{red.500}',
          iconColor: '{surface.500}',
          shadow: 'none',
        },
        overlay: {
          select: {
            background: '{surface.0}',
            borderColor: '{surface.200}',
            color: '{text.color}',
          },
          popover: {
            background: '{surface.0}',
            borderColor: '{surface.200}',
            color: '{text.color}',
          },
          modal: {
            background: '{surface.0}',
            borderColor: '{surface.200}',
            color: '{text.color}',
          },
        },
      },
      dark: {
        surface: {
          0: '#ffffff',
          50: '{zinc.50}',
          100: '{zinc.100}',
          200: '{zinc.200}',
          300: '{zinc.300}',
          400: '{zinc.400}',
          500: '{zinc.500}',
          600: '{zinc.600}',
          700: '{zinc.700}',
          800: '{zinc.800}',
          900: '{zinc.900}',
          950: '{zinc.950}',
        },
        primary: {
          color: '{primary.400}',
          contrastColor: '{surface.950}',
          hoverColor: '{primary.300}',
          activeColor: '{primary.200}',
        },
        highlight: {
          background: '{primary.400}',
          focusBackground: '{primary.300}',
          color: '{surface.950}',
          focusColor: '{surface.950}',
        },
        text: {
          color: '{surface.0}',
          hoverColor: '{surface.0}',
          mutedColor: '{surface.400}',
          hoverMutedColor: '{surface.300}',
        },
        content: {
          background: '{surface.900}',
          hoverBackground: '{surface.800}',
          borderColor: '{surface.700}',
          color: '{text.color}',
          hoverColor: '{text.hoverColor}',
        },
        formField: {
          background: '{surface.950}',
          disabledBackground: '{surface.800}',
          filledBackground: '{surface.900}',
          filledHoverBackground: '{surface.900}',
          filledFocusBackground: '{surface.950}',
          borderColor: '{surface.700}',
          hoverBorderColor: '{surface.600}',
          focusBorderColor: '{primary.color}',
          invalidBorderColor: '{red.400}',
          color: '{surface.0}',
          disabledColor: '{surface.500}',
          placeholderColor: '{surface.500}',
          invalidPlaceholderColor: '{red.400}',
          floatLabelColor: '{surface.500}',
          floatLabelFocusColor: '{primary.color}',
          floatLabelActiveColor: '{surface.500}',
          floatLabelInvalidColor: '{red.400}',
          iconColor: '{surface.400}',
          shadow: 'none',
        },
        overlay: {
          select: {
            background: '{surface.900}',
            borderColor: '{surface.700}',
            color: '{text.color}',
          },
          popover: {
            background: '{surface.900}',
            borderColor: '{surface.700}',
            color: '{text.color}',
          },
          modal: {
            background: '{surface.900}',
            borderColor: '{surface.700}',
            color: '{text.color}',
          },
        },
      },
    },
  },
  components: {
    button: {
      root: {
        borderRadius: '10px',
        paddingX: '1rem',
        paddingY: '0.625rem',
        label: {
          fontWeight: '600',
        },
        focusRing: {
          width: '{focus.ring.width}',
          style: '{focus.ring.style}',
          offset: '{focus.ring.offset}',
        },
      },
    },
    card: {
      root: {
        background: '{surface.0}',
        borderRadius: '12px',
        color: '{text.color}',
        shadow: '0 8px 24px rgba(15, 23, 42, 0.06)',
      },
      body: {
        padding: '1.5rem',
        gap: '1rem',
      },
      caption: {
        gap: '0.25rem',
      },
      title: {
        fontSize: '1rem',
        fontWeight: '600',
      },
      subtitle: {
        color: '{text.muted.color}',
      },
    },
    datatable: {
      root: {
        borderColor: '{surface.200}',
      },
      header: {
        background: '{surface.0}',
        borderColor: '{surface.200}',
        color: '{text.color}',
        borderWidth: '0 0 1px 0',
        padding: '1rem 1.25rem',
      },
      headerCell: {
        background: '{surface.0}',
        hoverBackground: '{surface.50}',
        selectedBackground: '{surface.100}',
        borderColor: '{surface.200}',
        color: '{surface.700}',
        hoverColor: '{surface.900}',
        selectedColor: '{surface.900}',
        gap: '0.5rem',
        padding: '0.875rem 1rem',
      },
      columnTitle: {
        fontWeight: '600',
      },
      row: {
        background: '{surface.0}',
        hoverBackground: '{surface.50}',
        selectedBackground: '{primary.50}',
        color: '{text.color}',
        hoverColor: '{text.color}',
        selectedColor: '{text.color}',
      },
      bodyCell: {
        borderColor: '{surface.200}',
        padding: '0.875rem 1rem',
      },
      footer: {
        background: '{surface.0}',
        color: '{text.muted.color}',
        padding: '1rem 1.25rem',
        borderColor: '{surface.200}',
      },
      footerCell: {
        borderColor: '{surface.200}',
        padding: '0.875rem 1rem',
      },
    },
    dialog: {
      root: {
        background: '{surface.0}',
        borderColor: '{surface.200}',
        color: '{text.color}',
        borderRadius: '12px',
        shadow: '0 20px 40px rgba(15, 23, 42, 0.12)',
      },
      header: {
        padding: '1.25rem 1.25rem 0 1.25rem',
        gap: '0.75rem',
      },
      title: {
        fontSize: '1rem',
        fontWeight: '600',
      },
      content: {
        padding: '1rem 1.25rem 1.25rem 1.25rem',
      },
      footer: {
        padding: '0 1.25rem 1.25rem 1.25rem',
        gap: '0.75rem',
      },
    },
    drawer: {
      root: {
        background: '{surface.0}',
        borderColor: '{surface.200}',
        color: '{text.color}',
        shadow: '0 20px 40px rgba(15, 23, 42, 0.12)',
      },
      header: {
        padding: '1rem 1rem 0 1rem',
      },
      title: {
        fontSize: '1rem',
        fontWeight: '600',
      },
      content: {
        padding: '1rem',
      },
      footer: {
        padding: '0 1rem 1rem 1rem',
      },
    },
    menu: {
      root: {
        background: '{surface.0}',
        borderColor: '{surface.200}',
        color: '{text.color}',
        borderRadius: '12px',
        shadow: '0 16px 32px rgba(15, 23, 42, 0.1)',
      },
      list: {
        padding: '0.25rem',
        gap: '0.125rem',
      },
      item: {
        focusBackground: '{surface.50}',
        color: '{text.color}',
        focusColor: '{surface.950}',
        padding: '0.625rem 0.75rem',
        borderRadius: '8px',
        gap: '0.625rem',
        icon: {
          color: '{text.muted.color}',
          focusColor: '{surface.700}',
        },
      },
      submenuLabel: {
        padding: '0.5rem 0.75rem',
        fontWeight: '600',
        background: 'transparent',
        color: '{text.muted.color}',
      },
      separator: {
        borderColor: '{surface.200}',
      },
    },
    panelmenu: {
      root: {
        gap: '0.5rem',
      },
      panel: {
        background: 'transparent',
        borderColor: 'transparent',
        borderWidth: '0',
        color: '{text.color}',
        padding: '0',
        borderRadius: '0',
        first: {
          borderWidth: '0',
          topBorderRadius: '0',
        },
        last: {
          borderWidth: '0',
          bottomBorderRadius: '0',
        },
      },
      item: {
        focusBackground: '{surface.50}',
        color: '{text.color}',
        focusColor: '{surface.950}',
        gap: '0.625rem',
        padding: '0.75rem',
        borderRadius: '10px',
        icon: {
          color: '{text.muted.color}',
          focusColor: '{surface.700}',
        },
      },
      submenu: {
        indent: '0.75rem',
      },
      submenuIcon: {
        color: '{text.muted.color}',
        focusColor: '{surface.700}',
      },
    },
    skeleton: {
      root: {
        borderRadius: '10px',
        background: '{surface.100}',
        animationBackground:
          'linear-gradient(90deg, {surface.100} 25%, {surface.200} 37%, {surface.100} 63%)',
      },
    },
    tag: {
      root: {
        fontSize: '0.75rem',
        fontWeight: '600',
        padding: '0.35rem 0.625rem',
        gap: '0.35rem',
        borderRadius: '999px',
        roundedBorderRadius: '999px',
      },
      icon: {
        size: '0.75rem',
      },
      primary: {
        background: '{primary.50}',
        color: '{primary.700}',
      },
      secondary: {
        background: '{surface.100}',
        color: '{surface.700}',
      },
      success: {
        background: '{green.50}',
        color: '{green.700}',
      },
      info: {
        background: '{sky.50}',
        color: '{sky.700}',
      },
      warn: {
        background: '{amber.50}',
        color: '{amber.700}',
      },
      danger: {
        background: '{red.50}',
        color: '{red.700}',
      },
      contrast: {
        background: '{surface.900}',
        color: '{surface.0}',
      },
    },
    toast: {
      root: {
        width: '24rem',
        borderRadius: '12px',
        borderWidth: '1px',
      },
      icon: {
        size: '1rem',
      },
      content: {
        padding: '1rem',
        gap: '0.75rem',
      },
      text: {
        gap: '0.25rem',
      },
      summary: {
        fontWeight: '600',
        fontSize: '0.9375rem',
      },
      detail: {
        fontWeight: '400',
        fontSize: '0.875rem',
      },
      closeButton: {
        width: '2rem',
        height: '2rem',
        borderRadius: '999px',
        focusRing: {
          width: '{focus.ring.width}',
          style: '{focus.ring.style}',
          offset: '{focus.ring.offset}',
        },
      },
      closeIcon: {
        size: '0.875rem',
      },
      info: {
        background: '{sky.50}',
        borderColor: '{sky.200}',
        color: '{sky.900}',
        detailColor: '{sky.700}',
        shadow: '0 16px 32px rgba(2, 132, 199, 0.12)',
      },
      success: {
        background: '{green.50}',
        borderColor: '{green.200}',
        color: '{green.900}',
        detailColor: '{green.700}',
        shadow: '0 16px 32px rgba(22, 163, 74, 0.12)',
      },
      warn: {
        background: '{amber.50}',
        borderColor: '{amber.200}',
        color: '{amber.900}',
        detailColor: '{amber.800}',
        shadow: '0 16px 32px rgba(217, 119, 6, 0.12)',
      },
      error: {
        background: '{red.50}',
        borderColor: '{red.200}',
        color: '{red.900}',
        detailColor: '{red.700}',
        shadow: '0 16px 32px rgba(220, 38, 38, 0.12)',
      },
    },
    tabs: {
      tablist: {
        background: '{surface.0}',
        borderWidth: '0 0 1px 0',
        borderColor: '{surface.200}',
      },
      tab: {
        background: 'transparent',
        hoverBackground: '{surface.50}',
        activeBackground: 'transparent',
        borderWidth: '0',
        borderColor: 'transparent',
        hoverBorderColor: 'transparent',
        activeBorderColor: 'transparent',
        color: '{text.muted.color}',
        hoverColor: '{text.color}',
        activeColor: '{primary.700}',
        padding: '0.875rem 1rem',
        fontWeight: '600',
        margin: '0',
        gap: '0.5rem',
        focusRing: {
          width: '{focus.ring.width}',
          style: '{focus.ring.style}',
          color: '{focus.ring.color}',
          offset: '2px',
          shadow: 'none',
        },
      },
      tabpanel: {
        background: '{surface.0}',
        color: '{text.color}',
        padding: '1.25rem 0 0 0',
      },
      activeBar: {
        height: '2px',
        bottom: '0',
        background: '{primary.600}',
      },
    },
  },
});
