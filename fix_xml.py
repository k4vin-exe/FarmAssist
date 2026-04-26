import xml.etree.ElementTree as ET
import os

def fix_xml(file_path):
    # Read as text first to keep comments and declaration
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    lines = content.split('\n')
    new_lines = []
    
    for line in lines:
        if '<string name=' in line and '%' in line:
            # check if % is not part of %1$s or %d
            if not ('%1$' in line or '%2$' in line or '%s' in line or '%d' in line):
                # add formatted="false" BEFORE >
                if 'formatted="false"' not in line:
                    line = line.replace('">', '" formatted="false">')
        new_lines.append(line)
        
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(new_lines))
    print(f"Fixed {file_path}")

fix_xml(r'app\src\main\res\values\strings.xml')
fix_xml(r'app\src\main\res\values-ta\strings.xml')
